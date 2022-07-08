package com.ad.ecom.user.profile.service;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.dto.AddressDto;
import com.ad.ecom.user.dto.UserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public interface ProfileService {
    ResponseEntity<ResponseMessage> getUserAccountInfo();
    ResponseEntity<ResponseMessage> updateUserInfo(UserInfoDto userInfoDto);
    ResponseEntity<ResponseMessage> deleteUserAccount();
    ResponseEntity<ResponseMessage> deleteUserAccountConfirmation(HttpSession httpSession, String token);
    ResponseEntity<ResponseMessage> updatePassword();
    ResponseEntity<ResponseMessage> updateEmail();
    ResponseEntity<ResponseMessage> storeAddresses(List<AddressDto> addressList);
    ResponseEntity<ResponseMessage> updateAddress(AddressDto addressDto);
    ResponseEntity<ResponseMessage> setDefaultAddress(String addressId);
}
