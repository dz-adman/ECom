package com.ad.ecom.core.security.config;

import com.ad.ecom.common.dto.AuthResponse;
import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.ecomuser.persistance.EComUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EComAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger LOGGER = LogManager.getLogger(EComAuthSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private EComUserLoginContext eComUserLoginContext;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        System.out.println();
        LOGGER.info("User '" + authentication.getName() + "' entered the workspace with role " + authentication.getAuthorities());
        LOGGER.info(authentication.getDetails());
        System.out.println();

        this.initializeEComUserContext(httpServletRequest, authentication);

        ResponseMessage responseMessage = new ResponseMessage();
        AuthResponse authResponse = AuthResponse.builder().isAuthenticated(true).role(((EComUser)authentication.getPrincipal()).getRole()).build();
        responseMessage.addResponse(ResponseType.SUCCESS, "AUTH SUCCESS");
        responseMessage.setResponseData(authResponse);

        httpServletResponse.setStatus(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getOutputStream().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage));
    }

    private void initializeEComUserContext(HttpServletRequest httpServletRequest, Authentication authentication) {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(httpServletRequest.getServletContext());
        EComUserLoginContext eComUserLoginContext = context.getBean(EComUserLoginContext.class);

        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
        eComUserLoginContext.setUserInfo(EComUser.class.cast(authentication.getPrincipal()));
        eComUserLoginContext.setLoggedInAt(new Date(System.currentTimeMillis()));
        eComUserLoginContext.setLoggedInFrom(userAgent.getBrowser().getName());
        eComUserLoginContext.setLoggedInTo(userAgent.getOperatingSystem().getDeviceType().getName());
        eComUserLoginContext.setLoginIp(httpServletRequest.getRemoteAddr());
        eComUserLoginContext.setLoginOs(userAgent.getOperatingSystem().getName());
        eComUserLoginContext.setReferer(httpServletRequest.getHeader("Referer"));
        eComUserLoginContext.setAuthorities(authentication.getAuthorities());
    }
}
