package com.codergoo.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 在自定义异常的错误码和信息时，如果过多，没有统一管理，则会出现重复。
 * 使用枚举统一管理code和message：
 *
 * @author coderGoo
 * @date 2020/12/16
 */

@Getter
@AllArgsConstructor
public enum ResultEnum {

    UNKNOW_ERROR(400, "未知错误"),
    SERVER_ERROR(500, "服务器错误"),
    SUCCESS(200, "请求成功");
    private Integer code;
    private String msg;
}
