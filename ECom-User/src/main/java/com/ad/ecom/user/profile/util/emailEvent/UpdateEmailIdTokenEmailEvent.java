package com.ad.ecom.user.profile.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EComUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateEmailIdTokenEmailEvent extends ApplicationEvent {
    private EComUser user;

    public UpdateEmailIdTokenEmailEvent(Object source, EComUser user) {
        super(source);
        this.user = user;
    }
}
