package com.dl2.fyp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper=false)
public class ServiceException extends RuntimeException {
    private HttpStatus code;

    public ServiceException(HttpStatus code, String msg){
        super(msg);
        this.code = code;
    }
}
