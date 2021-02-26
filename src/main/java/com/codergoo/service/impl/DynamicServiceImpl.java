package com.codergoo.service.impl;

import com.codergoo.domain.Dynamic;
import com.codergoo.domain.DynamicLikes;
import com.codergoo.domain.DynamicResource;
import com.codergoo.domain.User;
import com.codergoo.mapper.DynamicMapper;
import com.codergoo.mapper.DynamicResourceMapper;
import com.codergoo.service.DynamicResourceService;
import com.codergoo.service.DynamicService;
import com.codergoo.vo.DynamicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    public DynamicResourceService dynamicResourceService;
    
    @Autowired
    public DynamicMapper dynamicMapper;
    
    @Autowired
    public DynamicResourceMapper dynamicResourceMapper;
    
    @Override
    public DynamicVo addDynamic(Dynamic dynamic, MultipartFile[] resourceList) {
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
        
        // 如果有传输资源的话
        if (resourceList != null && resourceList.length > 0) {
            // 5. 图片存储
            int index = 0; // 用于记录图片下标
            for (MultipartFile resource : resourceList) {
                // 生成动态资源文件的名称
                String oldFileName = resource.getOriginalFilename();
                // 用于控制空文件数组上传的情况
                if (null == oldFileName || "".equals(oldFileName)) {
                    break;
                }
                String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
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
        }
        
        // 6. 给 dynamicVo 添加 dynamicResourceList
        DynamicVo dynamicVo = new DynamicVo();
        dynamicVo.setResourceList(dynamicResourceList);
        BeanUtils.copyProperties(dynamic, dynamicVo); // 属性转换
        // 7. 返回数据
        return dynamicVo;
    }
    
    @Override
    public Boolean removeDynamic(Integer id, Integer uid) {
        // 1. 判断动态是否属于该用户
        Integer hasDynamic = dynamicMapper.hasDynamic(uid, id);
        if (hasDynamic != 1) {
            log.error("hasDynamic: " + hasDynamic);
            throw new RuntimeException("数据存在异常，请联系开发人员紧急维护！");
        }
        
        // 2. 删除动态资源
        // 2.1 获取动态资源列表
        List<DynamicResource> dynamicResourceList = dynamicResourceMapper.listByDid(id);
        // 2.2 移除动态资源文件
        for (DynamicResource dynamicResource : dynamicResourceList) {
            File file = new File(dynamicResource.getSrc());
            // 判断文件是否存在
            if (file.exists()) {
                file.delete(); // 删除文件
            }
            // 从数据库只移除当前资源记录
            dynamicResourceMapper.removeDynamicResource(dynamicResource.getId());
        }
        log.info("dynamicResourceList: " + dynamicResourceList);
        // 3. 移除动态
        Integer delInteger = dynamicMapper.removeDynamic(id);
        return delInteger == 1;
    }
    
    @Override
    public Boolean updateDynamicPermissions(Integer id, Integer uid, Integer permissions) {
        // 1. 判断动态是否属于该用户
        Integer hasDynamic = dynamicMapper.hasDynamic(uid, id);
        if (hasDynamic != 1) {
            log.error("hasDynamic: " + hasDynamic);
            throw new RuntimeException("数据存在异常，请联系开发人员紧急维护！");
        }
        
        Integer integer = dynamicMapper.updateDynamicPermissions(id, permissions);
        return integer == 1;
    }
    
    @Override
    public List<DynamicVo> selfDynamicList(Integer uid) {
        // 1. 获取动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamicByUid(uid);
        
        // 2. 中间数据转换处理 PO => VO
        List<DynamicVo> resultDynamicList = new LinkedList<>();
        dynamicList.forEach(dynamic -> {
            DynamicVo dynamicVo = new DynamicVo();
            // 属性转换
            BeanUtils.copyProperties(dynamic, dynamicVo);
    
            // 动态资源
            List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
            dynamicVo.setResourceList(dynamicResourceList);
            // 动态评论
            dynamicVo.setDiscussesList(dynamic.getDiscussList());
            // 动态点赞
            List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
            dynamicVo.setLikesList(dynamicLikesList);
            
            resultDynamicList.add(dynamicVo);
        });
        
        // 3. 返回动态动态列表
        return resultDynamicList;
    }
    
    @Override
    public Integer getMaxId() {
        Integer id = dynamicMapper.getMaxId();
        return  id == null ? 1 : id + 1;
    }
    
    @Override
    public DynamicVo getDynamicById(Integer id) {
        Dynamic dynamic = dynamicMapper.findById(id);
        if (null == dynamic) {
            throw new RuntimeException("获取失败，查询不到该动态信息！");
        }
        
        if (1 != dynamic.getPermissions()) {
            throw new RuntimeException("获取失败，该动态设置了查看权限！");
        }
        
        DynamicVo dynamicVo = new DynamicVo();
        // 属性转换
        BeanUtils.copyProperties(dynamic, dynamicVo);
        // 动态资源转换
        List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
        dynamicVo.setResourceList(dynamicResourceList);
        // 动态评论
        dynamicVo.setDiscussesList(dynamic.getDiscussList());
        // 动态点赞
        List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
        dynamicVo.setLikesList(dynamicLikesList);
        
        return dynamicVo;
    }
}
