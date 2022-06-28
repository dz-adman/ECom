package com.ad.ecom.core;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class EComExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(EComExceptionHandler.class);
    private final ResponseMessage responseMessage = new ResponseMessage();

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<ResponseMessage> defaultRuntimeExceptionHandler(RuntimeException rex) {
        responseMessage.clearAll();
        LOGGER.error(rex.getMessage());
        rex.printStackTrace();
        responseMessage.addResponse(ResponseType.ERROR, "Internal Error!");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ResponseMessage> defaultExceptionHandler(Exception ex) {
        responseMessage.clearAll();
        LOGGER.error(ex.getMessage());
        ex.printStackTrace();
        responseMessage.addResponse(ResponseType.ERROR, "Internal Error!");
        return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
