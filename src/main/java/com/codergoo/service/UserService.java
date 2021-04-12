package com.codergoo.service;

import com.codergoo.domain.User;
import com.codergoo.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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
    Boolean updateUserAvatar(User user, MultipartFile avatar);
    
    // 根据id获取用户
    User findUserById(Integer id);
    
    // 返回用户信息
    UserVo findById(Integer id);
    
    List<UserVo> listUserByNickname(String nickname);
}
