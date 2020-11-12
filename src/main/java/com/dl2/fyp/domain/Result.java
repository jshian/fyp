package com.dl2.fyp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Result<T> {
    // error code
    private Integer code;
    // error message
    private String msg;
    // content
    private T data;

}
