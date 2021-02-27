package com.codergoo.service.impl;

import com.codergoo.domain.DynamicLikes;
import com.codergoo.mapper.DynamicLikesMapper;
import com.codergoo.service.DynamicLikesService;
import com.codergoo.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author coderGoo
 * @date 2021/2/26
 */
@Service
@Slf4j
public class DynamicLikesServiceImpl implements DynamicLikesService {
    
    @Autowired
    public DynamicLikesMapper dynamicLikesMapper;
    
    @Autowired
    public DynamicService dynamicService;
    
    @Override
    public Boolean addLikes(Integer uid, Integer did) {
        // 1. 判断是否已经存在该点赞记录
        DynamicLikes dynamicLikes = dynamicLikesMapper.findByDidAndUid(did, uid);
        // 存在则无法添加记录
        if (null != dynamicLikes) {
            return false;
        }
        
        dynamicLikes = new DynamicLikes();
        dynamicLikes.setDid(did);
        dynamicLikes.setUid(uid);
        // 设置时间
        dynamicLikes.setTime(new Date(System.currentTimeMillis()));
        
        // 执行添加
        Integer integer = dynamicLikesMapper.addLikes(dynamicLikes);
        if (integer == 1) {
            // 添加热度
            dynamicService.addDynamicHot(dynamicLikes.getDid(), 10.0);
            return true;
        }
        return false;
    }
    
    @Override
    public Boolean removeLikes(Integer uid, Integer did) {
        // 1. 判断是否已经存在该点赞记录
        DynamicLikes dynamicLikes = dynamicLikesMapper.findByDidAndUid(did, uid);
        // 不存在则无法移除记录
        if (null == dynamicLikes) {
            return false;
        }
    
        Integer integer = dynamicLikesMapper.removeLikes(dynamicLikes.getId());
        return integer == 1;
    }
}
