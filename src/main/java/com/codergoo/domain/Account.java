package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 账户实体类
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    
    private Integer id; // id
    private String username; // 用户名
    private String password; // 密码
    private Integer type; // 注册类型
    private Integer status; // 账号状态
}
