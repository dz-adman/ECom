package com.ad.ecom.util;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataRange<T> {
    private T lowerBound;
    private T upperBound;
}
