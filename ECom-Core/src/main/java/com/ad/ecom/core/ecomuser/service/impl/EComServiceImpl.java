package com.ad.ecom.core.ecomuser.service.impl;

import com.ad.ecom.core.ecomuser.repository.EcomUserRepository;
import com.ad.ecom.core.ecomuser.service.EComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class EComServiceImpl implements EComService, UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "User with loginId '%s' not found!";

    @Autowired
    private EcomUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginIdAndDeletedFalse(loginId).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, loginId)));
    }
}
