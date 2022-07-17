package com.ad.ecom.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataRange<T> {
    private T lowerBound;
    private T upperBound;
}
