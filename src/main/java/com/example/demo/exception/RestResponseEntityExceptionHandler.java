package com.example.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    // TODO: 2/10/20  методы получились одинаковые и самое главное статус  один и тот же. Я бы сделал с этим что-то
    //  и добавил бы ResponseStatusException.
    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Object> handleConflict(Throwable ex, WebRequest request) {
//        HttpClientErrorException.Unauthorized unauthorized = new HttpClientErrorException.Unauthorized();
//        HttpStatus statusCode = unauthorized.getStatusCode();
        return handleExceptionInternal(new RuntimeException(), ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleConflict(ServiceException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
