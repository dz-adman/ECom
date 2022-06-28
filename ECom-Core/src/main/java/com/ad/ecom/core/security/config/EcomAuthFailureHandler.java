package com.ad.ecom.core.security.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EcomAuthFailureHandler implements AuthenticationFailureHandler {

    private final Logger LOGGER = LogManager.getLogger(EcomAuthFailureHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println();
        LOGGER.info("User failed to enter in workspace.\nCause: " + e);
        System.out.println();

        try {
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/login?invalid-session=true");
        } catch (Exception ex) {
            // TODO Redirect to Error Page
            ex.printStackTrace();
        }
    }
}
