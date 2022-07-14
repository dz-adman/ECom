package com.ad.ecom.core.exception;

import com.ad.ecom.common.AuthResponse;
import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.exception.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class EComExceptionHandler extends ResponseEntityExceptionHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LogManager.getLogger(EComExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> userAlreadyExistsException(UserAlreadyExistsException exception) {
        LOGGER.error(exception.getClass().getName() + " [" + exception.getMessage() + "]");
        exception.printStackTrace();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.addResponse(ResponseType.ERROR, exception.getMessage());
        responseMessage.addResponse(ResponseType.ERROR, "User Profile Already Exists!");
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(exception.getClass().getName() + " [" + exception.getMessage() + "]");
        exception.printStackTrace();
        ResponseMessage responseMessage = new ResponseMessage();
        for(FieldError error : exception.getBindingResult().getFieldErrors())
            responseMessage.addResponse(ResponseType.ERROR, error.getDefaultMessage());
        for(ObjectError error : exception.getBindingResult().getGlobalErrors())
            responseMessage.addResponse(ResponseType.ERROR, error.getDefaultMessage());

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseMessage> nullPointerException(NullPointerException exception) {
        return yate(exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> fallBackException(Exception exception) {
        return yate(exception);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        LOGGER.error(accessDeniedException.getClass().getName() + " [" + accessDeniedException.getMessage() + "]");
        accessDeniedException.printStackTrace();
        ResponseMessage responseMessage = new ResponseMessage();
        if(accessDeniedException instanceof InvalidCsrfTokenException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AuthResponse authResponse = AuthResponse.builder().isAuthenticated(false)
                                                    .message(accessDeniedException.getMessage()).build();
            ObjectMapper objectMapper = new ObjectMapper();
            responseMessage.addResponse(ResponseType.ERROR, "Invalid CSRF-Token");
            responseMessage.setResponseData(authResponse);
            response.getOutputStream().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            AuthResponse authResponse = AuthResponse.builder().isAuthenticated(false)
                                                    .message(accessDeniedException.getMessage()).build();
            ObjectMapper objectMapper = new ObjectMapper();
            responseMessage.addResponse(ResponseType.ERROR, "Access Denied!");
            responseMessage.setResponseData(authResponse);
            response.getOutputStream().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage));
        }
    }

    /** DB ExceptionHandler
     * @param exception
     * @return ResponseEntity
     */
    @ExceptionHandler({ConstraintViolationException.class, HibernateException.class, JDBCException.class})
    public ResponseEntity<ResponseMessage> dbExceptionHandler(DataIntegrityViolationException exception) {
        return yate(exception);
    }


    /**
     * Yet Another Technical Exception
     * @param exception
     * @return ResponseEntity
     */
    private ResponseEntity<ResponseMessage> yate(Exception exception) {
        LOGGER.error(exception.getClass().getName() + " [" + exception.getMessage() + "]");
        exception.printStackTrace();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.addResponse(ResponseType.ERROR, exception.getMessage());
        responseMessage.addResponse(ResponseType.ERROR, "Some Internal Technical Error Occurred!");
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
