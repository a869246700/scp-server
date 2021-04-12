package com.codergoo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author coderGoo
 * @date 2021/3/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    
    private Integer id; // id
    private String nickname; // 昵称
    private Integer school; // 学校
    private String address; // 地址
    private String email; // 邮箱
    private String avatar; // 头像
    private Integer gender; // 性别
    private String signature; // 签名，简介
    private Date birthday; // 生日
    private String fullname; // 全名
    private Integer likes; // 点赞数
    private Integer fans; // 粉丝数
    private Integer follows; // 关注数
}
