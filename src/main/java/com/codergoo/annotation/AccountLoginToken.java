package com.codergoo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 用于标记需要请求携带token，并且进行校验的方法
 *
 * @author coderGoo
 * @date 2021/2/17
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountLoginToken {

    boolean required() default true;
}
