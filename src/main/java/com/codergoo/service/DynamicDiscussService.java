package com.codergoo.service;

import com.codergoo.domain.DynamicDiscuss;

/**
 * 动态评论业务逻辑接口
 *
 * @author coderGoo
 * @date 2021/2/25
 */
public interface DynamicDiscussService {
    
    // 添加动态评论
    Boolean addDynamicDiscuss(DynamicDiscuss dynamicDiscuss);
    
    // 删除动态评论
    Boolean removeDynamicDiscuss(Integer id, Integer uid);
}
