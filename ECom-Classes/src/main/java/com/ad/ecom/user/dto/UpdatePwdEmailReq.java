package com.ad.ecom.user.dto;

import lombok.Getter;

@Getter
public class UpdatePwdEmailReq {
    private String newPassword;
    private String newEmailId;
    private String secureToken;
}
