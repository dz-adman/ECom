package com.ad.ecom.core.security.config;

import com.ad.ecom.common.dto.AuthResponse;
import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EcomAuthFailureHandler implements AuthenticationFailureHandler {

    private final Logger LOGGER = LogManager.getLogger(EcomAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException, ServletException {
        System.out.println();
        LOGGER.info("User failed to enter in workspace.\nCause: " + ex);
        System.out.println();

        ResponseMessage responseMessage = new ResponseMessage();
        AuthResponse authResponse = AuthResponse.builder().isAuthenticated(false).message(ex.getMessage()).build();
        responseMessage.addResponse(ResponseType.SUCCESS, "AUTH FAILED!");
        responseMessage.setResponseData(authResponse);

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getOutputStream().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage));
    }
}
