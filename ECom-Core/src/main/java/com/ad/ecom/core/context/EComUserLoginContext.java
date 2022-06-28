package com.ad.ecom.core.context;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Date;

@Getter
@Setter
@ToString
@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EComUserLoginContext {
    private EcomUser userInfo;
    private Date loggedInAt;
    private String loggedInFrom;
    private String loggedInTo;
    private String loginIp;
    private String loginOs;
    private String referer;
}
