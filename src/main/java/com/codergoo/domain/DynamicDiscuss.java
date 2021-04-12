package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 动态评论实体类
 *
 * @author coderGoo
 * @date 2021/2/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicDiscuss implements Serializable {

    private Integer id; // 评论id
    private Integer uid; // 评论人id
    private Integer did; // 动态id
    private String content; // 评论内容
    private Integer pid; // 父评论id
    private Date time; // 评论时间

    private User user; // 评论人信息
    private List<DynamicDiscuss> children;
}
