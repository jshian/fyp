package com.dl2.fyp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
public class Result<T> {
    // error code
    private HttpStatus code;
    // error message
    private String msg;
    // content
    private T data;

}
