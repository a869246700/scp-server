package com.codergoo.common.utils;

import com.codergoo.common.entity.Result;
import com.codergoo.common.entity.ResultEnum;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 统一返回接口的工具类
 *
 * @author coderGoo
 * @date 2020/12/16
 */
public class ResultUtil<T> {

    public static Result success() {
        return success(null);
    }
    
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMsg());
        result.setSuccess(true);
        result.setData(object);
        return result;
    }

    public static Result success(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }
    
    public static Result success(int code, String message, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    public static Result error(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    public static Result error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(),resultEnum.getMsg());
    }
}
