package com.ad.ecom.user.profile.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private long addressId;
    @NotNull @NotBlank @NotEmpty
    private String addressType;
    @NotNull @NotBlank @NotEmpty
    private String houseAndStreetNum;
    private String landmark;
    @NotNull @NotBlank @NotEmpty
    private String city;
    @NotNull @NotBlank @NotEmpty
    private String state;
    @NotNull @NotBlank @NotEmpty
    private String country;
    @NotNull @NotBlank @Length(min = 6)
    private String zipCode;
}
