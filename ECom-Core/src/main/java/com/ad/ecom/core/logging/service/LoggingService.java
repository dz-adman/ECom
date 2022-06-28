package com.ad.ecom.core.logging.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoggingService {
    public void logRequest(HttpServletRequest httpServletRequest, Object body);
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body);
}