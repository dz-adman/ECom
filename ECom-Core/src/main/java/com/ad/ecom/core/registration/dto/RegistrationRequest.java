package com.ad.ecom.core.registration.dto;

import com.ad.ecom.core.ecomuser.stubs.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RegistrationRequest {
    @NotNull @NotEmpty @NotBlank
    private String loginId;
    @NotNull @NotEmpty @NotBlank
    private String firstName;
    @NotNull @NotEmpty @NotBlank
    private String lastName;
    @NotNull @NotEmpty @NotBlank
    private String email;
    @NotNull @NotEmpty @NotBlank
    private String password;
    @NotNull
    private Role role;
}
