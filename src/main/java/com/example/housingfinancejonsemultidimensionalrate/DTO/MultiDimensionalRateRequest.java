package com.example.housingfinancejonsemultidimensionalrate.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class MultiDimensionalRateRequest {
    private String numOfRows;	//한 페이지 결과 수
    private String pageNo;	//페이지 번호
    private String loanYm;	//대출연월
    private String cbGrd;	//CB등급
    private String jobCd;	//직업코드
    private String houseTycd;	//주택유형
    private String age;	//연령대
    private String income;	//연소득
    private String debt;	//부채
}
