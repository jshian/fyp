package com.dl2.fyp.aop;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IOException.class})
    public Result basicException(HttpServletResponse response, Exception ex){
        return ResultUtil.error(HttpStatus.UNAUTHORIZED,ex.getMessage());
    }

    @ExceptionHandler({NullPointerException.class,IllegalArgumentException.class})
    public Result nullException(HttpServletResponse response, Exception ex){
        return ResultUtil.error(HttpStatus.BAD_REQUEST, "invalid input");
    }

    @ExceptionHandler({AuthenticationException.class})
    public Result handleAuthenticationException(AuthenticationException e) {
        return ResultUtil.error(HttpStatus.UNAUTHORIZED,e.getMessage());
    }
}
