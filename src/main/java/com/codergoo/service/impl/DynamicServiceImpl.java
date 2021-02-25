package com.codergoo.service.impl;

import com.codergoo.domain.Dynamic;
import com.codergoo.domain.DynamicResource;
import com.codergoo.mapper.DynamicMapper;
import com.codergoo.mapper.DynamicResourceMapper;
import com.codergoo.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author coderGoo
 * @date 2021/2/24
 */
@Service
@Slf4j
public class DynamicServiceImpl implements DynamicService {
    
    @Value("${web.upload.dynamic-resource}")
    public String dynamicResourceUpload;
    
    @Autowired
    public DynamicMapper dynamicMapper;
    
    @Autowired
    public DynamicResourceMapper dynamicResourceMapper;
    
    @Override
    public Dynamic addDynamic(Dynamic dynamic, MultipartFile[] resourceList) {
        List<String> dynamicResourceList = new LinkedList<>(); // 保存的动态资源信息
        
        // 1. 获取id
        Integer id = this.getMaxId(); // 获取id
        // 2. 设置基础默认信息
        dynamic.setId(id); // 表单最大id
        dynamic.setType(1); // 默认：1
        dynamic.setTag(0); // 标签暂未决定：默认：0
        dynamic.setStatus(1); // 默认：1
        dynamic.setTime(new Date(System.currentTimeMillis())); // 当前时间
    
        // 3. 保存动态信息
        dynamicMapper.addDynamic(dynamic);
        
        // 4. 判断保存路径是否为空
        File folder = new File(dynamicResourceUpload);
        // 目录判断, 如果路径不存在重新生成
        if (!folder.isDirectory()) {
            log.info("不存在目标路径，生成文件夹：" + dynamicResourceUpload);
            folder.mkdirs();
        }
        
        // 5. 图片存储
        Integer index = 0; // 用于记录图片下标
        for (MultipartFile resource : resourceList) {
            // 生成动态资源文件的名称
            String oldFilePath = resource.getOriginalFilename();
            String suffix = oldFilePath.substring(oldFilePath.lastIndexOf("."));
            String fileName = "dynamic" + dynamic.getId() + "-resource-" +  index + suffix; // 文件名称
            String filePath = dynamicResourceUpload + "/" + fileName;
    
            try {
                // 6. 保存动态资源文件
                resource.transferTo(new File(filePath));
        
                // 7. 将动态资源路径保存到 DynamicResource 表中
                DynamicResource dynamicResource = new DynamicResource();
                dynamicResource.setDid(dynamic.getId());
                dynamicResource.setSrc(filePath);
                dynamicResource.setType(1); // 默认为：1（图片）
                Integer integer = dynamicResourceMapper.addDynamicResource(dynamicResource);
                if (integer != 1) {
                    throw new RuntimeException("图片保存失败，请检查资源问题！");
                }
                dynamicResourceList.add(filePath);
                index++; // 下标+1
            } catch (IOException e) {
                throw new RuntimeException("图片保存失败，请检查资源问题！");
            }
        }
        
        // 6. 给 dynamic 添加 dynamicResourceList
        dynamic.setResourceList(dynamicResourceList);
        return dynamic;
    }
    
    @Override
    public Boolean removeDynamic(Integer id) {
        return null;
    }
    
    @Override
    public Integer getMaxId() {
        Integer id = dynamicMapper.getMaxId();
        return  id == null ? 1 : id + 1;
    }
}
