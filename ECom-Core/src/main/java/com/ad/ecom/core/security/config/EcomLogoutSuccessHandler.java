package com.ad.ecom.core.security.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EcomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final Logger LOGGER = LogManager.getLogger(EcomLogoutSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println();
        LOGGER.info("User '" + authentication.getName() + "' left the workspace with role " + authentication.getAuthorities());
        LOGGER.info(authentication.getDetails());
        System.out.println();

        try {
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/login");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
