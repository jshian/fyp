package com.dl2.fyp.aop;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({NullPointerException.class, AuthenticationException.class, IOException.class})
    public Result basicException(HttpServletResponse response, Exception ex){
        log.info("error code:{} error_type:{}",response.getStatus(),ex.getClass());
        return ResultUtil.error(response.getStatus(),ex.getMessage());
    }
}
