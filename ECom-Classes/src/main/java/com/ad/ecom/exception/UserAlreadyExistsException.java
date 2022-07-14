package com.ad.ecom.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String rootCause;
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
    public UserAlreadyExistsException(String msg, String rootcause) {
        super(msg);
        this.rootCause = rootcause;
    }
}