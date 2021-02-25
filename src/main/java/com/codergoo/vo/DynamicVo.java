package com.codergoo.vo;

import com.codergoo.domain.DynamicDiscuss;
import com.codergoo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 动态
 *
 * @author coderGoo
 * @date 2021/2/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicVo {
    
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
    
    private List<String> resourceList; // 动态资源列表
    // private List<User> dynamicLikesList; // 点赞人列表
    private List<DynamicDiscuss> discussesList; // 动态评论列表
}
