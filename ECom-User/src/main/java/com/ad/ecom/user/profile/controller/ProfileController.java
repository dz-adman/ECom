package com.ad.ecom.user.profile.controller;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.dto.AddressDto;
import com.ad.ecom.user.dto.UserInfoDto;
import com.ad.ecom.user.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(path = "/info/fetch")
    public ResponseEntity<ResponseMessage> getUserAccountInfo() {
        return profileService.getUserAccountInfo();
    }

    @PatchMapping(path = "/info/update")
    public ResponseEntity<ResponseMessage> updateUserInfo(@RequestBody @NotNull UserInfoDto userInfoDto) {
        return profileService.updateUserInfo(userInfoDto);
    }

    @PatchMapping(path = "/delete")
    public ResponseEntity<ResponseMessage> deleteUserAccount(HttpSession httpSession) {
        return profileService.deleteUserAccount(httpSession);
    }

    @PatchMapping(path = "/info/update/pwd")
    public ResponseEntity<ResponseMessage> updatePassword() {
        return profileService.updatePassword();
    }

    @PatchMapping(path = "/info/update/email")
    public ResponseEntity<ResponseMessage> updateEmail() {
        return profileService.updateEmail();
    }

    @PostMapping(path = "/info/add/address")
    public ResponseEntity<ResponseMessage> storeAddresses(@NotNull @RequestBody List<AddressDto> addressList) {
        return profileService.storeAddresses(addressList);
    }

    @PatchMapping(path = "/info/update/address")
    public ResponseEntity<ResponseMessage> updateAddress(@NotNull @RequestBody AddressDto address) {
        return profileService.updateAddress(address);
    }

    @PostMapping(path = "/info/update/defaultAddress")
    public ResponseEntity<ResponseMessage> setDefaultAddress(@NotNull @NotBlank @NotEmpty @RequestBody String addressId) {
        return profileService.setDefaultAddress(addressId);
    }

}
