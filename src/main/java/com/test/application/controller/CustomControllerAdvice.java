package com.test.application.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomControllerAdvice extends DefaultHandlerExceptionResolver
{
    @ExceptionHandler
    public void handleIllegalArgumentException(HttpServletResponse response, IllegalArgumentException e) throws IOException
    {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
