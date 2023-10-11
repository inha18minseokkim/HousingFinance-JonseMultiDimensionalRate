package com.example.housingfinancejonsemultidimensionalrate.Job;

import com.example.housingfinancejonsemultidimensionalrate.ApiReceive.MultiDimensionalRateReceive;
import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateItem;
import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateRequest;
import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateResponse;
import com.example.housingfinancejonsemultidimensionalrate.Entity.MultiDimensionalRate;
import com.example.housingfinancejonsemultidimensionalrate.Repository.MultiDimensionalRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfiguration {
    @Value("${env.loadYm}")
    private String loanYearMonth;
    private final MultiDimensionalRateReceive receiver;

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MultiDimensionalRateRepository repository;
    @Bean
    public Job multiDimensionalRateJob() {
        return new JobBuilder("multiDimensionalRateJob",jobRepository)
                .start(multiDimensionalRateStep())
                .build();
    }
    @Bean
    public Step multiDimensionalRateStep() {
        return new StepBuilder("multiDimensionalRateStep",jobRepository)
                .<MultiDimensionalRateRequest,List<MultiDimensionalRate>>chunk(100,platformTransactionManager)
                .reader(requestCodeReader())
                .processor(compositeProcessor())
                .writer(itemWriter())
        .build();
    }
    @Bean
    public ItemReader<MultiDimensionalRateRequest> requestCodeReader() {
        String sqlStatement = "select" +
                "\n 100 as numOfRows" +
                "\n,1 as pageNo" +
                "\n," +loanYearMonth + " as loanYm" +
                "\n,ageCode as age" +
                "\n,creditGrade as cbGrd" +
                "\n,debtCode as debt" +
                "\n,houseCode as houseTycd" +
                "\n,incomeCode as income" +
                "\n,jobCode as jobCd" +
                "\nfrom" +
                "    \nAgeInfo,CreditBreauInfo,DebtInfo,HouseTypeInfo,IncomeInfo,JobInfo"
                + "\nwhere HouseTypeInfo.description like '%오피%'\n" +
                "  and AgeInfo.ageCode between 2 and 2\n" +
                "  and DebtInfo.debtCode <= 1\n" +
                "  and IncomeInfo.incomeCode <= 4 and 5";
        log.info(sqlStatement);
        return new JdbcCursorItemReaderBuilder<MultiDimensionalRateRequest>()
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(MultiDimensionalRateRequest.class))
                .sql(sqlStatement)
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<MultiDimensionalRateRequest,List<MultiDimensionalRate>> compositeProcessor() {
        CompositeItemProcessor<MultiDimensionalRateRequest, List<MultiDimensionalRate>> objectObjectCompositeItemProcessor = new CompositeItemProcessor<>();
        objectObjectCompositeItemProcessor.setDelegates(Arrays.asList(itemReceiveProcessor(),itemConvertProcessor()));
        return objectObjectCompositeItemProcessor;
    }

    @Bean
    public ItemProcessor<MultiDimensionalRateRequest, List<Map.Entry<MultiDimensionalRateRequest,MultiDimensionalRateItem>>> itemReceiveProcessor() {
        return item -> {
            MultiDimensionalRateResponse multiDimensionalRateResponse = receiver.apiReceive(item);
            if(!multiDimensionalRateResponse.getHeader().getResultCode().equals("00"))
                throw new RuntimeException("API 수신 실패" + item);
            return multiDimensionalRateResponse.getBody().getItems()
                    .stream().map((element) ->
                            (Map.Entry<MultiDimensionalRateRequest,MultiDimensionalRateItem>)new AbstractMap.SimpleEntry<MultiDimensionalRateRequest,MultiDimensionalRateItem>(item,element)).toList();
        };
    }
    private Long parseLong(String target){
        try{
            return Long.parseLong(target);
        } catch (NullPointerException e){
            return 0L;
        }
    }
    private Double parseDouble(String target){
        try{
            return Double.parseDouble(target);
        } catch (NullPointerException e){
            return 0.0;
        }
    }
    @Bean
    public ItemProcessor<List<Map.Entry<MultiDimensionalRateRequest,MultiDimensionalRateItem>>,List<MultiDimensionalRate>> itemConvertProcessor() {
        return item -> item.stream()
                .map(
                        (Map.Entry<MultiDimensionalRateRequest,MultiDimensionalRateItem> fromItem) -> {
                            MultiDimensionalRateRequest key = fromItem.getKey();
                            MultiDimensionalRateItem value = fromItem.getValue();
                        return MultiDimensionalRate.builder()
                        .loadYearMonth(key.getLoanYm())
                        .creditGrade(parseLong(key.getCbGrd()))
                        .jobCode(key.getJobCd())
                        .houseCode(key.getHouseTycd())
                        .ageCode(Long.parseLong(key.getAge()))
                        .incomeCode(Long.parseLong(key.getIncome()))
                        .debtCode(Long.parseLong(key.getDebt()))
                        .averageLoanRate(parseDouble(value.getAvgLoanRat()))
                        .averageLoanRate2(parseDouble(value.getAvgLoanRat2()))
                        .bankName(value.getBankNm())
                        .loanCount(parseLong(value.getCnt()))
                        .loanAmount(parseLong(value.getLoanAmt()))
                        .maxLoanRate(parseDouble(value.getMaxLoanRat()))
                        .minLoanRate(parseDouble(value.getMinLoanRat()))
                        .build();})
                .toList();
    }

    @Bean
    public ItemWriter<List<MultiDimensionalRate>> itemWriter() {
        return chunk -> {
            List<MultiDimensionalRate> collect = chunk.getItems().stream().flatMap(List::stream).collect(Collectors.toList());
            repository.insertObjectBatch(collect);
        };
    }

}
