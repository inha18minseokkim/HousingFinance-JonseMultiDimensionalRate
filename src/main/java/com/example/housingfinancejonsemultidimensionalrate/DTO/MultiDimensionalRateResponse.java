package com.example.housingfinancejonsemultidimensionalrate.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MultiDimensionalRateResponse {
    private ResponseHeader header;
    private MultiDimensionalRateBody body;
}
