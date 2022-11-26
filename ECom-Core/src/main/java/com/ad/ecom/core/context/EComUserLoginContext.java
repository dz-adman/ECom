package com.ad.ecom.core.context;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import lombok.Data;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.Date;

@Data
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
    private Collection<? extends GrantedAuthority> authorities;
}
