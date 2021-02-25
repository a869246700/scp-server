package com.codergoo.service;

import com.codergoo.domain.Dynamic;
import com.codergoo.vo.DynamicVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 动态模块业务层接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
public interface DynamicService {
    
    // 添加动态
    DynamicVo addDynamic(Dynamic dynamic, MultipartFile[] resourceList);
    
    // 删除动态
    Boolean removeDynamic(Integer id, Integer uid);
    
    // 获取自己的动态列表
    List<DynamicVo> selfDynamicList(Integer uid);
    
    // 获取动态最大ID
    Integer getMaxId();
    
    // 修改动态的查看权限
    Boolean updateDynamicPermissions(Integer id, Integer uid, Integer permissions);
    
    // 根据动态id获取动态
    DynamicVo getDynamicById(Integer id);
}
