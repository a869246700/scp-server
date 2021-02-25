package com.codergoo.service;

import com.codergoo.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 用户信息 业务逻辑接口
 *
 * @author coderGoo
 * @date 2021/2/23
 */
public interface UserService {
    
    // 添加用户
    Boolean addUser(User user);
    
    // 修改用户信息
    Boolean updateUserInfo(User user);
    
    // 修改用户头像
    Boolean updateUserAvatar(Integer id, MultipartFile avatar);
    
    // 根据id获取用户
    User findUserById(Integer id);
}
