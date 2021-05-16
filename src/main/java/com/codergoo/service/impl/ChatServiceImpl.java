package com.codergoo.service.impl;

import com.codergoo.domain.ChatMsg;
import com.codergoo.mapper.ChatMapper;
import com.codergoo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author coderGoo
 * @date 2021/4/13
 */
@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    public ChatMapper chatMapper;
    
    @Override
    public List<ChatMsg> getChatRecord(Integer u1, Integer u2) {
        return chatMapper.getChatRecord(u1, u2);
    }
    
    @Override
    public Boolean delChatRecordRow(Integer id) {
        return 1 == chatMapper.delChatRecordRow(id);
    }
}
