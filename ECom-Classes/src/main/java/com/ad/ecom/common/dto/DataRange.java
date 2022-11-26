package com.ad.ecom.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataRange<T> {
    private T lowerBound;
    private T upperBound;
}
