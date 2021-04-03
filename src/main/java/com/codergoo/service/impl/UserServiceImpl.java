package com.codergoo.service.impl;

import com.codergoo.domain.User;
import com.codergoo.mapper.UserMapper;
import com.codergoo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 用户信息 业务逻辑实现类
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    public UserMapper userMapper;
    
    @Value("${web.upload.user-avatar}")
    public String userAvatarUpload;
    
    @Override
    public Boolean addUser(User user) {
        // 设置一些默认值
        user.setNickname("用户名" + user.getId());
        user.setGender(0); // 默认男，0：男，1：女
        Integer integer = userMapper.addUser(user);
        return integer != 0;
    }
    
    @Override
    public User findUserById(Integer id) {
        return userMapper.findById(id);
    }
    
    @Override
    public Boolean updateUserInfo(User user) {
        // 1. 获取旧数据
        User oldUser = userMapper.findById(user.getId());

        // 2. 比对哪些数据不需要修改
        user.setNickname(StringUtils.isBlank(user.getNickname()) ? oldUser.getNickname() : user.getNickname());
        user.setGender(user.getGender() == null ? oldUser.getGender() : user.getGender());
        user.setSchool(user.getSchool() == null ? oldUser.getSchool() : user.getSchool());
        user.setEmail(user.getEmail() == null ? oldUser.getEmail() : user.getEmail());
        user.setAddress(user.getAddress() == null ? oldUser.getAddress() : user.getAddress());
        user.setSignature(user.getSignature() == null ? oldUser.getSignature() : user.getSignature());
        user.setFullname(user.getFullname() == null ? oldUser.getFullname() : user.getFullname());
        user.setBirthday(user.getBirthday() == null ? oldUser.getBirthday() : user.getBirthday());
        user.setAvatar(user.getAvatar() == null ? oldUser.getAvatar() : user.getAvatar());
        
        // 判断修改次数
        Integer integer = userMapper.updateUser(user);
        if (integer == 0) {
            throw new RuntimeException("修改失败，不存在该用户信息！");
        } else if (integer > 1) {
            throw new RuntimeException("修改失败，数据存在异常！");
        }
        return true;
    }
    
    @Override
    public Boolean updateUserAvatar(User user, MultipartFile avatar) {
        String filePath; // 文件保存路径
        // 如果不为空
        if (!StringUtils.isBlank(user.getAvatar())) {
            filePath = user.getAvatar();
        } else {
            // 1. 判断保存路径是否为空
            File folder = new File(userAvatarUpload);
            // 目录判断, 如果路径不存在重新生成
            if (!folder.isDirectory()) {
                log.info("不存在目标路径，生成文件夹：" + userAvatarUpload);
                folder.mkdirs();
            }
    
            // 2. 生成用户头像文件的名称
            String oldFilePath = avatar.getOriginalFilename();
            assert oldFilePath != null;
            String suffix = oldFilePath.substring(oldFilePath.lastIndexOf("."));
            String fileName = "avatar-" + user.getId() + suffix;
            filePath = userAvatarUpload + "/" + fileName;
        }
        log.info("filePath: " + filePath);
        try {
            // 3. 保存头像文件
            avatar.transferTo(new File(filePath));
            
            // 4. 将路径保存到用户的avatar字段中去
            Integer integer = userMapper.updateUserAvatar(user.getId(), filePath);
            if (integer == 0) {
                throw new RuntimeException("修改失败，不存在该用户信息！");
            } else if (integer > 1) {
                throw new RuntimeException("修改失败，数据存在异常！");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
