package com.codergoo.service;

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
}
