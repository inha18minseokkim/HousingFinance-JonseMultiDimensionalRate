package com.example.housingfinancejonsemultidimensionalrate.DTO;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class MultiDimensionalRateBody {
    private Long pageNo;
    private Long totalCount;
    private Long numOfRows;
    private List<MultiDimensionalRateItem> items;
}
