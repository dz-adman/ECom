package com.ad.ecom.core.ecomuser.controller;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.core.ecomuser.service.EComService;
import com.ad.ecom.core.registration.util.WebTemplates;
import com.ad.ecom.user.dto.AddressDto;
import com.ad.ecom.user.dto.UserInfoDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/user")
public class EComUserController {
    @Value("${serverUrl}")
    private String serverUrl;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final Logger LOGGER = LogManager.getLogger(EComUserController.class);

    @Autowired
    private EComService eComService;

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

    @GetMapping(path = "/account/info")
    public ResponseEntity<ResponseMessage> getUserAccountInfo() {
        return eComService.getUserAccountInfo();
    }

    @PatchMapping(path = "/account/update/info")
    public ResponseEntity<ResponseMessage> updateUserInfo(@RequestBody @NotNull UserInfoDto userInfoDto) {
        return eComService.updateUserInfo(userInfoDto);
    }

    @PatchMapping(path = "/account/delete")
    public ResponseEntity<ResponseMessage> deleteUserAccount(HttpSession httpSession) {
        return eComService.deleteUserAccount(httpSession);
    }

    @PatchMapping(path = "/account/update/password")
    public ResponseEntity<ResponseMessage> updatePassword() {
        return eComService.updatePassword();
    }

    @PatchMapping(path = "/account/update/email")
    public ResponseEntity<ResponseMessage> updateEmail() {
        return eComService.updateEmail();
    }

    @PostMapping(path = "/address/add")
    public ResponseEntity<ResponseMessage> storeAddresses(@NotNull @RequestBody List<AddressDto> addressList) {
        return eComService.storeAddresses(addressList);
    }

    @PatchMapping(path = "/address/update")
    public ResponseEntity<ResponseMessage> updateAddress(@NotNull @RequestBody AddressDto address) {
        return eComService.updateAddress(address);
    }

    @PostMapping(path = "/address/update/default")
    public ResponseEntity<ResponseMessage> setDefaultAddress(@NotNull @NotBlank @NotEmpty @RequestBody String addressId) {
        return eComService.setDefaultAddress(addressId);
    }
}
