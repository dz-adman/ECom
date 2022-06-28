package com.ad.ecom.core.logging.service.impl;

import com.ad.ecom.core.logging.service.LoggingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingServiceImpl implements LoggingService {

    private final Logger LOGGER = LogManager.getLogger(LoggingServiceImpl.class);

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        System.out.println();
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        if (!parameters.isEmpty())
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        if (body != null)
            stringBuilder.append("body=[" + body + "] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("]");
        LOGGER.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("RESPONSE ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        if(body.toString().contains("html"))
            stringBuilder.append("responseBody=[<html_view>] ");
        else
            stringBuilder.append("responseBody=[").append(body.toString()).append("] ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("]");
        LOGGER.info(stringBuilder.toString());
        System.out.println();
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}