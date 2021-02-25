package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Account;
import com.codergoo.domain.User;
import com.codergoo.service.TokenService;
import com.codergoo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息模块接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    public UserService userService;
    
    @Autowired
    public TokenService tokenService;
    
    // 修改用户信息
    @PostMapping("/updateUserInfo")
    @AccountLoginToken
    public Result updateUserInfo(User user) {
        // id为空则报错
        if (StringUtils.isBlank(String.valueOf(user.getId()))) {
            return ResultUtil.error(500, "错误请求, 请携带用户ID");
        }
        
        // 执行修改
        Boolean updateSuccess = userService.updateUserInfo(user);
        if (updateSuccess) {
            return ResultUtil.success(200, "修改成功");
        }
        return ResultUtil.error(200, "修改失败!");
    }
    
    // 修改用户头像
    @PostMapping("/updateUserAvatar")
    @AccountLoginToken
    public Result updateUserAvatar(MultipartFile avatar, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // id为空则报错
        if (StringUtils.isBlank(String.valueOf(user.getId()))) {
            return ResultUtil.error(500, "错误请求, 请携带用户ID！");
        }
        // 文件上传为空则报错
        if (avatar == null || avatar.isEmpty()) {
            return ResultUtil.error(500, "上传头像失败, 请重新选择！");
        }

        // 执行修改
        Boolean updateSuccess = userService.updateUserAvatar(user.getId(), avatar);
        if (updateSuccess) {
            return ResultUtil.success(200, "修改头像成功");
        }
        return ResultUtil.error(200, "修改头像失败!");
    }
}
