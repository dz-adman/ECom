package com.ad.ecom.common.dto;

import com.ad.ecom.ecomuser.stubs.Role;
import lombok.Builder;

@Builder
public record AuthResponse(boolean isAuthenticated, Role role, String message) {}
