package com.codergoo.service.impl;

import com.codergoo.domain.Friend;
import com.codergoo.mapper.FriendMapper;
import com.codergoo.service.FriendService;
import com.codergoo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 好友业务实现类
 *
 * @author coderGoo
 * @date 2021/4/7
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService {
    
    @Autowired
    public FriendMapper friendMapper;
    
    @Autowired
    public MessageService messageService;
    
    @Override
    public List<Friend> listFriend(Integer uid) {
        return friendMapper.listFriend(uid);
    }
    
    public List<Friend> listAttention(Integer uid) {
        return friendMapper.listAttention(uid);
    }
    
    @Transactional
    @Override
    public Boolean addAttention(Integer fid, Integer uid) {
        Friend friend = new Friend();
        friend.setFid(fid);
        friend.setUid(uid);
        friend.setStatus(1); // 默认为1
        friend.setTime(new Date(System.currentTimeMillis()));
        
        messageService.addAttentionMessage(uid, fid); // 添加关注通知
        return 1 == friendMapper.addAttention(friend);
    }
    
    @Override
    public Boolean cancelAttention(Integer fid, Integer uid) {
        return 1 == friendMapper.removeAttention(fid, uid);
    }
    
    @Override
    public Integer getFollowsNumber(Integer uid) {
        return friendMapper.getAttentionNumber(uid);
    }
    
    @Override
    public Integer getFansNumber(Integer uid) {
        return friendMapper.getFansNumber(uid);
    }
}
