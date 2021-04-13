package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息通知实体类
 *
 * @author coderGoo
 * @date 2021/4/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    
    private Integer id;
    private Integer uid; // 接收的用户
    private String content; // 消息内容
    private Integer from; // 发送的用户
    private Integer did; // 动态id，可能是好友或者关注用户发布动态时携带
    private Integer type; // 通知类型，动态发布：1、点赞：2、评论：3、关注：4
    private Date time; // 时间
    
    private User fromUser;
}
