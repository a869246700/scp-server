package com.codergoo.service;

import com.codergoo.domain.Dynamic;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.multipart.MultipartFile;

/**
 * 动态模块业务层接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
public interface DynamicService {
    
    // 添加动态
    Dynamic addDynamic(Dynamic dynamic, MultipartFile[] resourceList);
    
    // 删除动态
    Boolean removeDynamic(Integer id);
    
    // 获取动态最大ID
    Integer getMaxId();
}
