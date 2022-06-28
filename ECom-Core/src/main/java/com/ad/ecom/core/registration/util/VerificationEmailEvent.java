package com.ad.ecom.core.registration.util;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VerificationEmailEvent extends ApplicationEvent {
    private EcomUser user;
    private String url;

    public VerificationEmailEvent(Object source, EcomUser user, String url) {
        super(source);
        this.user = user;
        this.url = url;
    }
}
