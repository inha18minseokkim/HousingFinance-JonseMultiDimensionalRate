package com.example.housingfinancejonsemultidimensionalrate.Entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultiDimensionalRate {
    private Long id;
    private String loadYearMonth;
    private Long creditGrade;
    private String jobCode;
    private String houseCode;
    private Long ageCode;
    private Long incomeCode;
    private Long debtCode;

    private Double averageLoanRate;
    private Double averageLoanRate2;
    private String bankName;
    private Long loanCount;
    private Long loanAmount;
    private Double maxLoanRate;
    private Double minLoanRate;
}