package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 动态附件资源实体类
 *
 * @author coderGoo
 * @date 2021/2/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicResource implements Serializable {

    private Integer id; // 资源id
    private Integer did; // 动态id
    private String src; // 资源存储地址
    private Integer type; // 资源类型
}
