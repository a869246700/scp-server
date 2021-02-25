package com.codergoo.service.impl;

import com.codergoo.domain.DynamicResource;
import com.codergoo.mapper.DynamicResourceMapper;
import com.codergoo.service.DynamicResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author coderGoo
 * @date 2021/2/25
 */
@Service
@Slf4j
public class DynamicResourceServiceImpl implements DynamicResourceService {
    
    @Autowired
    public DynamicResourceMapper dynamicResourceMapper;
    
    @Override
    public Boolean addDynamicResource(DynamicResource dynamicResource) {
        return null;
    }
    
    @Override
    public Boolean RemoveDynamicResource(Integer id) {
        return null;
    }
    
    @Override
    public List<String> listDynamicResourceByDid(Integer did) {
        // 1. 获取动态资源列表
        List<DynamicResource> dynamicResourceList = dynamicResourceMapper.listByDid(did);
        // 2. 返回动态资源数据
        List<String> resourceList = new LinkedList<>();
        for (DynamicResource dynamicResource : dynamicResourceList) {
            resourceList.add(dynamicResource.getSrc());
        }
        return resourceList;
    }
}
