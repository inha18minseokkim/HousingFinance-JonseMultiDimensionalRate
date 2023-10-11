package com.example.housingfinancejonsemultidimensionalrate.Repository;

import com.example.housingfinancejonsemultidimensionalrate.Entity.MultiDimensionalRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MultiDimensionalRateRepository {
    private final JdbcTemplate jdbcTemplate;
    public void insertObjectBatch(List<MultiDimensionalRate> rateList) {
        log.info(rateList.toString());
        String sql = "insert into MultiDimensionalRentLoanRateInfo (loadYearMonth, creditGrade, jobCode, houseCode, ageCode, incomeCode,\n" +
                "                                              debtCode, averageLoanRate, averageLoanRat2, bankName, loanCount,\n" +
                "                                              loanAmount, maxLoanRate, minLoanRate)\n" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MultiDimensionalRate dto = rateList.get(i);
                ps.setString(1, dto.getLoadYearMonth());
                ps.setLong(2, dto.getCreditGrade());
                ps.setString(3, dto.getJobCode());
                ps.setString(4, dto.getHouseCode());
                ps.setLong(5, dto.getAgeCode());
                ps.setLong(6, dto.getIncomeCode());
                ps.setLong(7, dto.getDebtCode());
                ps.setDouble(8, dto.getAverageLoanRate());
                ps.setDouble(9, dto.getAverageLoanRate2());
                ps.setString(10, dto.getBankName());
                ps.setLong(11, dto.getLoanCount());
                ps.setLong(12, dto.getLoanAmount());
                ps.setDouble(13, dto.getMaxLoanRate());
                ps.setDouble(14, dto.getMinLoanRate());
            }

            @Override
            public int getBatchSize() {
                return rateList.size();
            }
        });

    }
}
