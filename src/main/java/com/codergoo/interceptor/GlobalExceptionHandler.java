package com.codergoo.interceptor;

import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author coderGoo
 * @date 2021/2/18
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        if ("401".equals(msg)) {
            return ResultUtil.error(401, "token无效，请重新获取！");
        }
        return ResultUtil.error(500, msg);
    }
}
