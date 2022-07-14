package com.ad.ecom.core.security.config;

import com.ad.ecom.common.AuthResponse;
import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.ecomuser.persistance.EcomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
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

        ResponseMessage responseMessage = new ResponseMessage();
        AuthResponse authResponse = AuthResponse.builder().isAuthenticated(false).role(((EcomUser)authentication.getPrincipal()).getRole()).build();
        responseMessage.addResponse(ResponseType.SUCCESS, "LOGOUT SUCCESS");
        responseMessage.setResponseData(authResponse);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getOutputStream().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage));
    }
}
