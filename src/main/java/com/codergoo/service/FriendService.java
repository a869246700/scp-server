package com.codergoo.service;

import com.codergoo.domain.Friend;
import com.codergoo.domain.User;

import java.util.List;

/**
 * 好友业务接口
 *
 * @author coderGoo
 * @date 2021/4/7
 */
public interface FriendService {
    
    // 获取好友列表
    List<Friend> listFriend(Integer uid);
    
    // 获取关注列表
    List<Friend> listAttention(Integer uid);
    
    // 添加好友（关注）
    Boolean addAttention(Integer fid, Integer uid);
    
    // 删除好友（移除关注）
    Boolean cancelAttention(Integer fid, Integer uid);
    
    // 获取关注数量
    Integer getFollowsNumber(Integer uid);
    
    // 获取粉丝数量
    Integer getFansNumber(Integer uid);
}
