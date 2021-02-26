package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 动态实体类
 *
 * @author coderGoo
 * @date 2021/2/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dynamic implements Serializable {

    private Integer id; // 动态id
    private Integer uid; // 发表用户id
    private String content; // 内容
    private Integer type; // 类型
    private String camouflage; // 伪装
    private Integer tag; // 标签
    private Integer permissions; // 权限
    private Integer status; // 状态
    private String address; // 发表地址
    private Integer showAddress; // 是否显示发表地址
    private Date time; // 发布时间
    
    // private User user; // 发表的用户
    private List<DynamicDiscuss> discussList; // 动态评论
    private List<DynamicLikes> likesList; // 动态点赞
    private List<DynamicResource> resourceList; // 动态资源列表
}
