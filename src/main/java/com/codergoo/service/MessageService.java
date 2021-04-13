package com.codergoo.service;

import com.codergoo.domain.Message;

import java.util.List;

/**
 * 消息通知业务接口
 *
 * @author coderGoo
 * @date 2021/4/13
 */
public interface MessageService {
    
    // 获取用户消息通知
    List<Message> getMessage(Integer uid);
    
    // 删除用户消息通知
    Boolean delMessageRow(Integer id);
    
    // 添加动态发布通知
    Boolean addDynamicReleaseMessage(Integer did, Integer from);
    
    // 添加动态评论通知
    Boolean addDynamicDiscussMessage(Integer did, Integer from, Integer uid);
    
    // 添加动态点赞通知
    Boolean addDynamicLikeMessage(Integer did, Integer from, Integer uid);
    
    // 添加用户关注通知
    Boolean addAttentionMessage(Integer from, Integer uid);
}
