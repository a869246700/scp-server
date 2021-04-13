package com.codergoo.service.impl;

import com.codergoo.domain.Message;
import com.codergoo.mapper.MessageMapper;
import com.codergoo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息通知业务实现
 *
 * @author coderGoo
 * @date 2021/4/13
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    public MessageMapper messageMapper;
    
    @Override
    public List<Message> getMessage(Integer uid) {
        return messageMapper.getMessageList(uid);
    }
    
    @Override
    public Boolean delMessageRow(Integer id) {
        return 1 == messageMapper.delMessageById(id);
    }
    
    @Override
    public Boolean addDynamicReleaseMessage(Integer did, Integer from) {
        return null;
    }
    
    @Override
    public Boolean addDynamicDiscussMessage(Integer did, Integer from, Integer uid) {
        return null;
    }
    
    @Override
    public Boolean addDynamicLikeMessage(Integer did, Integer from, Integer uid) {
        return null;
    }
    
    @Override
    public Boolean addDynamicLikeMessage(Integer from, Integer uid) {
        return null;
    }
}
