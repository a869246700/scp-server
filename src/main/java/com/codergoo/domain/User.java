package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    
    private Integer id;
    private String nickname;
    private Integer school;
    private String address;
    private String email;
    private String avatar;
    private Integer gender;
    private String signature;
    private Date birthday;
    private String fullname;
}
