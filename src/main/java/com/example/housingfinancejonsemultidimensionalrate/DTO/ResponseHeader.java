package com.example.housingfinancejonsemultidimensionalrate.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ResponseHeader {
    private String resultCode;
    private String resultMsg;
}
