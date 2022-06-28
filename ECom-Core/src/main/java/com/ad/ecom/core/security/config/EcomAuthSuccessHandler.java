package com.ad.ecom.core.security.config;

import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EcomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger LOGGER = LogManager.getLogger(EcomAuthSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private EComUserLoginContext eComUserLoginContext;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println();
        LOGGER.info("User '" + authentication.getName() + "' entered the workspace with role " + authentication.getAuthorities());
        LOGGER.info(authentication.getDetails());
        System.out.println();

        this.initializeEComUserContext(httpServletRequest, authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();   // can also use 'var' here
        authorities.forEach(authority -> {
            if(authority.getAuthority().equals("ROLE_USER")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/user");
                } catch (Exception e) {
                    // TODO Redirect to Error Page
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equals("ROLE_ADMIN")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/admin");
                } catch (Exception e) {
                    // TODO Redirect to Error Page
                    e.printStackTrace();
                }
            } else if (authority.getAuthority().equals("ROLE_SELLER")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/seller");
                } catch (Exception ex) {
                    // TODO Redirect to Error Page
                    ex.printStackTrace();;
                }
            }
        });
    }

    private void initializeEComUserContext(HttpServletRequest httpServletRequest, Authentication authentication) {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(httpServletRequest.getServletContext());
        EComUserLoginContext eComUserLoginContext = context.getBean(EComUserLoginContext.class);

        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
        eComUserLoginContext.setUserInfo(EcomUser.class.cast(authentication.getPrincipal()));
        eComUserLoginContext.setLoggedInAt(new Date(System.currentTimeMillis()));
        eComUserLoginContext.setLoggedInFrom(userAgent.getBrowser().getName());
        eComUserLoginContext.setLoggedInTo(userAgent.getOperatingSystem().getDeviceType().getName());
        eComUserLoginContext.setLoginIp(httpServletRequest.getRemoteAddr());
        eComUserLoginContext.setLoginOs(userAgent.getOperatingSystem().getName());
        eComUserLoginContext.setReferer(httpServletRequest.getHeader("Referer"));
    }
}
