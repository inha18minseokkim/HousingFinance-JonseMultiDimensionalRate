package com.example.housingfinancejonsemultidimensionalrate.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class MultiDimensionalRateItem {
    private String avgLoanRat;	//산술평균대출금리
    private String avgLoanRat2;	//가중평균대출금리
    private String BankNm;	//은행명
    private String cnt;	//대출건수
    private String loanAmt;	//대출실행금액
    private String maxLoanRat;	//최대대출금리
    private String minLoanRat;	//최소대출금리
}
