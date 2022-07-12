package com.ad.ecom.user.profile.service.impl;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.core.security.PasswordEncoder;
import com.ad.ecom.ecomuser.persistance.EcomUser;
import com.ad.ecom.ecomuser.repository.EcomUserRepository;
import com.ad.ecom.registration.persistance.VerificationToken;
import com.ad.ecom.registration.repository.VerificationTokenRepository;
import com.ad.ecom.user.dto.AddressDto;
import com.ad.ecom.user.dto.UpdatePwdEmailReq;
import com.ad.ecom.user.dto.UserInfoDto;
import com.ad.ecom.user.persistance.Address;
import com.ad.ecom.user.profile.service.ProfileService;
import com.ad.ecom.user.profile.util.emailEvent.*;
import com.ad.ecom.user.repository.AddressRepository;
import com.ad.ecom.user.stubs.AddressType;
import com.ad.ecom.util.EComUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private EcomUserRepository userRepo;
    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public ResponseEntity<ResponseMessage> getUserAccountInfo() {
        ResponseMessage responseMessage = new ResponseMessage();
        UserInfoDto userInfoDto = new UserInfoDto();
        List<AddressDto> addresses = null;
        EcomUser ecomUser = loginContext.getUserInfo();
        Optional<List<Address>> addressList = addressRepo.findAllByUserId(ecomUser.getId());
        if(addressList.isPresent() && !addressList.get().isEmpty()) { // At-least 1 Address is there
            addresses = convertAddressesToDto(addressList.get());
            userInfoDto.setAddressInfo(addresses);
        }
        userInfoDto.setUserId(ecomUser.getId());
        userInfoDto.setFirstName(ecomUser.getFirstName());
        userInfoDto.setLastName(ecomUser.getLastName());
        userInfoDto.setEMail(ecomUser.getEmail());

        responseMessage.setResponseData(userInfoDto);
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    /**
     * Method to update firstName and lastName
     * @param userInfoDto
     * @return
     */
    @Override
    public ResponseEntity<ResponseMessage> updateUserInfo(UserInfoDto userInfoDto) {
        ResponseMessage responseMessage = new ResponseMessage();
        EcomUser user = loginContext.getUserInfo();
        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        userRepo.save(user);
        responseMessage.addResponse(ResponseType.SUCCESS, "Information Updated Successfully.");
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteUserAccount() {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<EcomUser> userData = userRepo.findByLoginIdAndDeletedFalse(loginContext.getUserInfo().getLoginId());
        if(userData.isPresent()) {
            // send token on user email
            eventPublisher.publishEvent(new AccountDelTokenEmailEvent(this, userData.get()));
            responseMessage.addResponse(ResponseType.SUCCESS, "Secure token for Account-Deletion sent to user email");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Some Error Occurred! Please try again.");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteUserAccountConfirmation(HttpSession httpSession, String token) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<VerificationToken> vToken = tokenRepo.findByToken(token);
        if(vToken.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid Token!");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            Timestamp currTime = new Timestamp(Calendar.getInstance().getTime().getTime());
            if(vToken.get().getExpiresOn().compareTo(currTime) <= 0) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Expired!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            if(vToken.get().isUsed()) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Already Used!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            // ELSE (Valid Token)

            // Delete Token from table
            tokenRepo.delete(vToken.get());
            // Delete User
            EcomUser user = vToken.get().getUser();
            user.setDeleted(true);
            userRepo.save(user);

            // End/Invalidate user session && Remove Auth-Info
            httpSession.invalidate();
            SecurityContextHolder.clearContext();

            responseMessage.addResponse(ResponseType.SUCCESS, "Account Deleted Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<ResponseMessage> updatePassword() {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<EcomUser> userData = userRepo.findByLoginIdAndDeletedFalse(loginContext.getUserInfo().getLoginId());
        if(userData.isPresent()) {
            // send token on user email
            eventPublisher.publishEvent(new UpdatePwdTokenEmailEvent(this, userData.get()));
            responseMessage.addResponse(ResponseType.SUCCESS, "Secure token for Password-Update sent to user email");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Some Error Occurred! Please try again.");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseMessage> updatePasswordConfirmation(HttpSession httpSession, UpdatePwdEmailReq request) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<VerificationToken> vToken = tokenRepo.findByToken(request.getSecureToken());
        if(vToken.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid Token!");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            Timestamp currTime = new Timestamp(Calendar.getInstance().getTime().getTime());
            if(vToken.get().getExpiresOn().compareTo(currTime) <= 0) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Expired!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            if(vToken.get().isUsed()) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Already Used!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            // ELSE (Valid Token)

            // update user password
            EcomUser user = vToken.get().getUser();
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepo.save(user);

            // send notification on user mail
            eventPublisher.publishEvent(new UpdatePwdEmailEvent(this, user));

            // End/Invalidate user session && Remove Auth-Info
            httpSession.invalidate();
            SecurityContextHolder.clearContext();

            responseMessage.addResponse(ResponseType.SUCCESS, "Password Updated Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity<ResponseMessage> updateEmail() {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<EcomUser> userData = userRepo.findByLoginIdAndDeletedFalse(loginContext.getUserInfo().getLoginId());
        if(userData.isPresent()) {
            // send token on user email
            eventPublisher.publishEvent(new UpdateEmailIdTokenEmailEvent(this, userData.get()));
            responseMessage.addResponse(ResponseType.SUCCESS, "Secure token for EmailId-Update sent to user email");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Some Error Occurred! Please try again.");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateEmailConfirmation(UpdatePwdEmailReq request) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<VerificationToken> vToken = tokenRepo.findByToken(request.getSecureToken());
        if(vToken.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid Token!");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            Timestamp currTime = new Timestamp(Calendar.getInstance().getTime().getTime());
            if(vToken.get().getExpiresOn().compareTo(currTime) <= 0) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Expired!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            if(vToken.get().isUsed()) {
                responseMessage.addResponse(ResponseType.ERROR, "Token Already Used!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            if(!EComUtil.INSTANCE.validateEmail(request.getNewEmailId())) {
                responseMessage.addResponse(ResponseType.ERROR, "Invalid EmailId!");
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
            // ELSE (Valid Token)

            // update user emailId
            EcomUser user = vToken.get().getUser();
            String oldEmailId = user.getEmail();
            user.setEmail(request.getNewEmailId());
            userRepo.save(user);

            // send notification on user mail
            eventPublisher.publishEvent(new UpdateEmailIdEmailEvent(this, user.getFirstName(), user.getLastName(), oldEmailId, user.getEmail()));

            responseMessage.addResponse(ResponseType.SUCCESS, "EmailId Updated Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> storeAddresses(List<AddressDto> addressList) {
        ResponseMessage responseMessage = new ResponseMessage();
        for(AddressDto addressDto : addressList) {
            try {
                Address address = new Address();
                address.setUserId(loginContext.getUserInfo().getId());
                address.setAddress(addressDto.getHouseAndStreetNum());
                address.setAddressType(AddressType.valueOf(addressDto.getAddressType()));
                address.setCity(addressDto.getCity());
                address.setState(addressDto.getState());
                address.setCountry(addressDto.getCountry());
                if (addressDto.getLandmark() != null) address.setLandmark(addressDto.getLandmark());
                // If no default address exists, set address to default address
                Optional<Address> defaultAddr = addressRepo.findByUserIdAndDefaultAddressTrue(loginContext.getUserInfo().getId());
                if(defaultAddr.isEmpty())   address.setDefaultAddress(true);
                addressRepo.save(address);
                responseMessage.addResponse(ResponseType.SUCCESS, "Address Added Successfully.");
            } catch (Exception exception) {
                responseMessage.addResponse(ResponseType.ERROR, "Unable to store Address. Invalid/Insufficient Data.");
            }
        }
        return new ResponseEntity(responseMessage, HttpStatus.MULTI_STATUS);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateAddress(AddressDto addressDto) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Address> addressData = addressRepo.findById(addressDto.getAddressId());
        if(addressData.isPresent()) {
            updateAddressParams(addressDto, addressData.get());
            addressRepo.save(addressData.get());
            responseMessage.addResponse(ResponseType.SUCCESS, "Address Information Updated Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Invalid Data.");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> setDefaultAddress(String addressId) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(addressRepo.existsById(Long.parseLong(addressId))) {
            Optional<List<Address>> addresses = addressRepo.findAllByUserId(loginContext.getUserInfo().getId());
            if(addresses.isPresent()) {
                updateDefaultAddress(addresses.get(), Long.parseLong(addressId));
                responseMessage.addResponse(ResponseType.SUCCESS, "Default Address Updated Successfully.");
                return new ResponseEntity(responseMessage, HttpStatus.OK);
            } else { // No Addresses Present
                responseMessage.addResponse(ResponseType.ERROR, "No Address Found for User.");
                return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
            }
        }
        // Default AddressId not Found
        responseMessage.addResponse(ResponseType.ERROR, "Invalid Data. Not Found.");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private List<AddressDto> convertAddressesToDto(List<Address> addresses) {
        List<AddressDto> addressList = null;
        if(!addresses.isEmpty()) {
            addressList = new ArrayList<>();
            for(Address addr : addresses) {
                AddressDto addressDto = new AddressDto();
                addressDto.setAddressId(addr.getId());
                addressDto.setHouseAndStreetNum(addr.getAddress());
                addressDto.setAddressType(addr.getAddressType().toString());
                addressDto.setCity(addr.getCity());
                addressDto.setState(addr.getState());
                addressDto.setCountry(addr.getCity());
                addressDto.setLandmark(addr.getLandmark());
                addressDto.setZipCode(String.valueOf(addr.getPinCode()));
                addressList.add(addressDto);
            }
        }
        return addressList;
    }

    private void updateAddressParams(AddressDto addressDto, Address address) {
        address.setAddress(addressDto.getHouseAndStreetNum());
        address.setAddressType(AddressType.valueOf(addressDto.getAddressType()));
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setPinCode(Integer.valueOf(addressDto.getZipCode()));
        if(addressDto.getLandmark() != null) address.setLandmark(addressDto.getLandmark());
    }

    private void updateDefaultAddress(List<Address> addresses, long addressId) {
        for(Address address : addresses) {
            address.setDefaultAddress(address.getId() == addressId ? true : false);
            addressRepo.save(address);
        }
    }
}
