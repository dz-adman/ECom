package com.ad.ecom.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRange<T> {
    private T lowerBound;
    private T upperBound;
}
