package com.ad.ecom.registration.dto;

import com.ad.ecom.ecomuser.stubs.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
