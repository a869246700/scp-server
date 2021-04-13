package com.codergoo.service.impl;

import com.codergoo.domain.DynamicDiscuss;
import com.codergoo.mapper.DynamicDiscussMapper;
import com.codergoo.service.DynamicDiscussService;
import com.codergoo.service.DynamicService;
import com.codergoo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Autowired
    public MessageService messageService;
    
    @Transactional
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
            // 添加邮件通知
            // 如果是对评论进行回复，则需要通知之前的评论人
            if (null != dynamicDiscuss.getPid()) {
                // 获取之前评论人id
                DynamicDiscuss preDiscuss = dynamicDiscussMapper.findById(dynamicDiscuss.getPid());
                messageService.addDynamicDiscussMessage(dynamicDiscuss.getDid(), dynamicDiscuss.getUid(), preDiscuss.getUid());
            }
            // 获取动态发布人id
            Integer uid = dynamicService.getUidByDid(dynamicDiscuss.getDid());
            messageService.addDynamicDiscussMessage(dynamicDiscuss.getDid(), dynamicDiscuss.getUid(), uid);
            
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
