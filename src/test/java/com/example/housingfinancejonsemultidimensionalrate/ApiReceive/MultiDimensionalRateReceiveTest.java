package com.example.housingfinancejonsemultidimensionalrate.ApiReceive;

import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateRequest;
import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultiDimensionalRateReceiveTest {
    @Autowired
    private MultiDimensionalRateReceive multiDimensionalRateReceive;

    @Test
    public void ApiReceiveTest() {
        MultiDimensionalRateRequest request = MultiDimensionalRateRequest.builder()
                .numOfRows("100")
                .pageNo("1")
                .loanYm("202310")
                .cbGrd("1")
                .jobCd("01")
                .houseTycd("10")
                .age("2")
                .income("5")
                .debt("1")
                .build();
        MultiDimensionalRateResponse multiDimensionalRateResponse = multiDimensionalRateReceive.apiReceive(request);
        System.out.println(multiDimensionalRateResponse);
    }
}