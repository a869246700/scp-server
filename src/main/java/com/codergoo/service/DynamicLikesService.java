package com.codergoo.service;

import com.codergoo.domain.DynamicLikes;

import java.util.List;

/**
 * 动态点赞业务接口
 *
 * @author coderGoo
 * @date 2021/2/26
 */
public interface DynamicLikesService {
    
    // 添加点赞
    Boolean addLikes(Integer uid, Integer did);
    
    // 删除点赞
    Boolean removeLikes(Integer uid, Integer did);
    
    // 获取用户的所有动态获赞数量
    Integer getAllDynamicLikesCount(Integer uid);
    
    // 获取用户的点赞列表
    List<DynamicLikes> getDynamicLikeList(Integer uid);
}
