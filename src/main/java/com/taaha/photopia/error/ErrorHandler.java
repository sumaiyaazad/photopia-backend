package com.taaha.photopia.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value() , "internal server error",  ex.getMessage(),
                request.getDescription(false).substring(4));
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.value(), "user not found", ex.getMessage(), request.getDescription(false).substring(4));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponse exceptionResponse = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.value(), "username not found", ex.getMessage(), request.getDescription(false).substring(4));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(),  HttpStatus.BAD_REQUEST.value(), "bad request", "validation failure", ex.getBindingResult().toString());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
