package com.codergoo.service.impl;

import com.codergoo.domain.DynamicDiscuss;
import com.codergoo.mapper.DynamicDiscussMapper;
import com.codergoo.service.DynamicDiscussService;
import com.codergoo.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author coderGoo
 * @date 2021/2/25
 */
@Service
@Slf4j
public class DynamicDiscussServiceImpl implements DynamicDiscussService {
    
    @Autowired
    public DynamicDiscussMapper dynamicDiscussMapper;
    
    @Autowired
    public DynamicService dynamicService;
    
    @Override
    public DynamicDiscuss addDynamicDiscuss(DynamicDiscuss dynamicDiscuss) {
        // 设置时间
        if (null == dynamicDiscuss.getTime()) {
            dynamicDiscuss.setTime(new Date(System.currentTimeMillis()));
        }
        
        // 设置id
        Integer id = getMaxId();
        dynamicDiscuss.setId(id);
        
        Integer integer = dynamicDiscussMapper.addDiscuss(dynamicDiscuss);
        if (1 == integer) {
            // 添加热度
            dynamicService.addDynamicHot(dynamicDiscuss.getDid(), 10.0);
            dynamicDiscuss = dynamicDiscussMapper.findById(id);
            return dynamicDiscuss;
        }
        return null;
    }
    
    @Override
    public Boolean removeDynamicDiscuss(Integer id, Integer uid) {
        // 判断是否是自己的评论
        DynamicDiscuss dynamicDiscuss = dynamicDiscussMapper.findById(id);
        if (null == dynamicDiscuss) {
            throw new RuntimeException("服务器出错，不存在该评论！");
        }
        
        if (!uid.equals(dynamicDiscuss.getUid())) {
            throw new RuntimeException("操作错误，不可以删除其他人的评论！");
        }
        
        Integer integer = dynamicDiscussMapper.removeDiscuss(id);
        return integer == 1;
    }
    
    @Override
    public Integer getMaxId() {
        Integer id = dynamicDiscussMapper.getMaxId();
        return  id == null ? 1 : id + 1;
    }
}
