package com.codergoo.common.entity;

import lombok.Data;

/**
 *
 * http请求最外层对象,统一返回接口
 * @param <T>
 * @author coderGoo
 * @date 2020/12/16
 */
@Data
public class Result<T> {

    // 返回状态码
    private int code;

    // 返回提示信息
    private String message;

    // 响应成功
    private boolean success;

    // 返回具体内容
    private T data;
}
