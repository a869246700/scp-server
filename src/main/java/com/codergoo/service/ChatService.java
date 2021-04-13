package com.codergoo.service;

import com.codergoo.domain.ChatMsg;

import java.util.List;

/**
 * 聊天业务接口
 *
 * @author coderGoo
 * @date 2021/4/13
 */
public interface ChatService {
    
    // 获取聊天记录
    List<ChatMsg> getChatRecord(Integer u1, Integer u2);
    
    // 删除聊天记录
    Boolean delChatRecordRow(Integer id);
}
