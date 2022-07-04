package com.ad.ecom.core.ecomuser.controller;

import com.ad.ecom.core.registration.util.WebTemplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/user")
public class EComUserController {
    @Value("${serverUrl}")
    private String serverUrl;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final Logger LOGGER = LogManager.getLogger(EComUserController.class);

    @RolesAllowed("USER")
    @GetMapping(path = "/user")
    public String userHomePage() {
        return WebTemplates.UserWebTemplate(serverUrl, contextPath);
    }

    @RolesAllowed("ADMIN")
    @GetMapping(path = "/admin")
    public String adminHomePage() {
        return WebTemplates.AdminWebTemplate(serverUrl, contextPath);
    }

    @RolesAllowed("SELLER")
    @GetMapping(path = "/seller")
    public String sellerHomePage() { return WebTemplates.SellerWebTemplate(serverUrl, contextPath); }

}
