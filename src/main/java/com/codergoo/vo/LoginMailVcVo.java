package com.codergoo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱登录 接口参数
 *
 * @author coderGoo
 * @date 2021/3/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMailVcVo {
    
    private String username;
    private String loginVc;
}
