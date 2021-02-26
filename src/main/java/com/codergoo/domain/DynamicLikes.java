package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 动态点赞实体类
 *
 * @author coderGoo
 * @date 2021/2/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicLikes implements Serializable {

    private Integer id; // 点赞id
    private Integer uid; // 点赞用户id
    private Integer did; // 动态id

    private User user; // 点赞人信息
}
