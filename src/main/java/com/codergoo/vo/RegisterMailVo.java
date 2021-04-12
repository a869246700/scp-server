package com.codergoo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件注册 接口参数
 *
 * @author coderGoo
 * @date 2021/3/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMailVo {
    
    private String username;
    private String registerVc;
}
