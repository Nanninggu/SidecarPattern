package com.design_pattern.sidecar_pattern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private RestTemplate restTemplate;

    @ExceptionHandler(Exception.class)
    public final void handleAllExceptions(Exception ex, WebRequest request) {
        String sidecarServiceUrl = "http://localhost:8081/log";
        String errorMessage = ex.getMessage();
        restTemplate.postForObject(sidecarServiceUrl, errorMessage, String.class);
    }
}
