package com.ad.ecom.user.profile.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdatePwdEmailEvent extends ApplicationEvent {
    private EcomUser user;
    public UpdatePwdEmailEvent(Object source, EcomUser user) {
        super(source);
        this.user = user;
    }
}
