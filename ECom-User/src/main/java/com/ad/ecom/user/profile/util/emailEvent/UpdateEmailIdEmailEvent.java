package com.ad.ecom.user.profile.util.emailEvent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateEmailIdEmailEvent extends ApplicationEvent {
    private String firstName;
    private String lastName;
    private String oldEmailId;
    private String newEmailId;

    public UpdateEmailIdEmailEvent(Object source, String firstName, String lastName, String oldEmailId, String newEmailId) {
        super(source);
        this.firstName = firstName;
        this.lastName = lastName;
        this.oldEmailId = oldEmailId;
        this.newEmailId = newEmailId;
    }
}
