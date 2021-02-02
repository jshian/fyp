package com.dl2.fyp.util;

import com.dl2.fyp.domain.Result;
import org.springframework.http.HttpStatus;

public class ResultUtil {
    public static Result success(Object o){
        Result result = new Result();
        result.setCode(HttpStatus.OK);
        result.setMsg("success");
        result.setData(o);
        return result;
    }
    public static Result success(){
        return success(null);
    }
    public static Result error(HttpStatus code,String msg){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
