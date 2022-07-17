package com.ad.ecom.user.profile.util.emailEvent;

import com.ad.ecom.ecomuser.persistence.EcomUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateEmailIdTokenEmailEvent extends ApplicationEvent {
    private EcomUser user;

    public UpdateEmailIdTokenEmailEvent(Object source, EcomUser user) {
        super(source);
        this.user = user;
    }
}
