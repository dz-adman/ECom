package com.ad.ecom.user.profile.service.impl;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import com.ad.ecom.core.ecomuser.repository.EcomUserRepository;
import com.ad.ecom.user.dto.AddressDto;
import com.ad.ecom.user.dto.UserInfoDto;
import com.ad.ecom.user.persistance.Address;
import com.ad.ecom.user.profile.service.ProfileService;
import com.ad.ecom.user.repository.AddressRepository;
import com.ad.ecom.user.stubs.AddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private EcomUserRepository userRepository;
    @Autowired
    private EComUserLoginContext eComUserLoginContext;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public ResponseEntity<ResponseMessage> getUserAccountInfo() {
        ResponseMessage responseMessage = new ResponseMessage();
        UserInfoDto userInfoDto = new UserInfoDto();
        List<AddressDto> addresses = null;
        EcomUser ecomUser = eComUserLoginContext.getUserInfo();
        Optional<List<Address>> addressList = addressRepository.findAllByUserId(ecomUser.getId());
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

    @Override
    public ResponseEntity<ResponseMessage> updateUserInfo(UserInfoDto userInfoDto) {
        ResponseMessage responseMessage = new ResponseMessage();
        EcomUser user = eComUserLoginContext.getUserInfo();
        user.setFirstName(userInfoDto.getFirstName());
        user.setLastName(userInfoDto.getLastName());
        userRepository.save(user);
        responseMessage.addResponse(ResponseType.SUCCESS, "Information Updated Successfully.");
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    // TODO: Make changes to apply 2FA by Email
    @Override
    public ResponseEntity<ResponseMessage> deleteUserAccount(HttpSession httpSession) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<EcomUser> userData = userRepository.findByLoginIdAndDeletedFalse(eComUserLoginContext.getUserInfo().getLoginId());
        if(userData.isPresent()) {
            userData.get().setDeleted(true);
            userRepository.save(userData.get());
            // End/Invalidate user session
            httpSession.invalidate();
            responseMessage.addResponse(ResponseType.SUCCESS, "Account Deleted Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Some Error Occurred! Please try again.");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO: Complete this method using 2FA by Email
    @Override
    public ResponseEntity<ResponseMessage> updatePassword() {
        return null;
    }

    // TODO: Complete this method using 2FA by Email
    @Override
    public ResponseEntity<ResponseMessage> updateEmail() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> storeAddresses(List<AddressDto> addressList) {
        ResponseMessage responseMessage = new ResponseMessage();
        for(AddressDto addressDto : addressList) {
            try {
                Address address = new Address();
                address.setUserId(eComUserLoginContext.getUserInfo().getId());
                address.setAddress(addressDto.getHouseAndStreetNum());
                address.setAddressType(AddressType.valueOf(addressDto.getAddressType()));
                address.setCity(addressDto.getCity());
                address.setState(addressDto.getState());
                address.setCountry(addressDto.getCountry());
                if (addressDto.getLandmark() != null) address.setLandmark(addressDto.getLandmark());
                addressRepository.save(address);
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
        Optional<Address> addressData = addressRepository.findById(addressDto.getAddressId());
        if(addressData.isPresent()) {
            updateAddressParams(addressDto, addressData.get());
            addressRepository.save(addressData.get());
            responseMessage.addResponse(ResponseType.SUCCESS, "Address Information Updated Successfully.");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "Invalid Data.");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> setDefaultAddress(String addressId) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(addressRepository.existsById(Long.parseLong(addressId))) {
            Optional<List<Address>> addresses = addressRepository.findAllByUserId(eComUserLoginContext.getUserInfo().getId());
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
            addressRepository.save(address);
        }
    }
}
