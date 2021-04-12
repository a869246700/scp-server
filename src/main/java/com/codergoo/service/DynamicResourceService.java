package com.codergoo.service;

import com.codergoo.domain.DynamicResource;

import java.util.List;

/**
 * 动态资源业务逻辑
 *
 * @author coderGoo
 * @date 2021/2/25
 */
public interface DynamicResourceService {
    
    // 添加动态资源
    Boolean addDynamicResource(DynamicResource dynamicResource);
    
    // 删除动态资源
    Boolean RemoveDynamicResource(Integer id);
    
    // 根据动态id返回动态资源列表
    List<String> listDynamicResourceByDid(Integer did);
}
