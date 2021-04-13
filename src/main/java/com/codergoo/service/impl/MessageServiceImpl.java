package com.codergoo.service.impl;

import com.codergoo.domain.Friend;
import com.codergoo.domain.Message;
import com.codergoo.mapper.MessageMapper;
import com.codergoo.service.FriendService;
import com.codergoo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    
    @Autowired
    public FriendService friendService;
    
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
        // 获取粉丝列表
        List<Friend> friendList = friendService.listAttention(from);
        // 给所有粉丝发送消息
        friendList.forEach(friend -> {
            Message message = new Message();
            message.setDid(did);
            message.setUid(friend.getFid());
            message.setSid(from);
            message.setTime(new Date(System.currentTimeMillis()));
            message.setType(1);
            message.setContent("发布一条新动态");
            message.setStatus(0);
            messageMapper.insertMessage(message);
        });
        
        return true;
    }
    
    @Override
    public Boolean addDynamicDiscussMessage(Integer did, Integer from, Integer uid) {
        // 如果评论人和被评论人是同一人则不发送
        if (from.equals(uid)) {
            return true;
        }
        Message message = new Message();
        message.setDid(did);
        message.setSid(from);
        message.setUid(uid);
        message.setTime(new Date(System.currentTimeMillis()));
        message.setType(3);
        message.setContent("评论了你");
        message.setStatus(0);
        return 1 == messageMapper.insertMessage(message);
    }
    
    @Override
    public Boolean addDynamicLikeMessage(Integer did, Integer from, Integer uid) {
        // 如果点赞人和动态发布人是同一人则不发送
        if (from.equals(uid)) {
            return true;
        }
        Message message = new Message();
        message.setDid(did);
        message.setSid(from);
        message.setUid(uid);
        message.setTime(new Date(System.currentTimeMillis()));
        message.setType(2);
        message.setContent("点赞了你");
        message.setStatus(0);
        return 1 == messageMapper.insertMessage(message);
    }
    
    @Override
    public Boolean addAttentionMessage(Integer from, Integer uid) {
        Message message = new Message();
        message.setSid(from);
        message.setUid(uid);
        message.setTime(new Date(System.currentTimeMillis()));
        message.setType(4);
        message.setContent("关注了你");
        message.setStatus(0);
        return 1 == messageMapper.insertMessage(message);
    }
}
