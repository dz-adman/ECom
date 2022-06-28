package com.ad.ecom.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserInfoDto {
    private long userId;
    @NotNull @NotBlank @NotEmpty
    private String firstName;
    @NotNull @NotBlank @NotEmpty
    private String lastName;
    private String eMail;
    private List<AddressDto> addressInfo;
}
