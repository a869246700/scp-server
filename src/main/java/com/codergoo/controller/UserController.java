package com.codergoo.controller;

import com.alibaba.fastjson.JSONObject;
import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Account;
import com.codergoo.domain.User;
import com.codergoo.service.DynamicLikesService;
import com.codergoo.service.TokenService;
import com.codergoo.service.UserService;
import com.codergoo.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    
    @Autowired
    public DynamicLikesService dynamicLikesService;
    
    // 修改用户信息
    @PostMapping("/updateUserInfo")
    @AccountLoginToken
    public Result updateUserInfo(@RequestBody User user, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        user.setId(tokenService.getUserByToken(token).getId());
        
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
        
        // 文件上传为空则报错
        if (avatar == null || avatar.isEmpty()) {
            return ResultUtil.error(500, "上传头像失败, 请重新选择！");
        }

        // 执行修改
        Boolean updateSuccess = userService.updateUserAvatar(user, avatar);
        if (updateSuccess) {
            return ResultUtil.success(200, "修改头像成功");
        }
        return ResultUtil.error(200, "修改头像失败!");
    }
    
    // 获取用户信息
    @GetMapping("/getUser")
    @AccountLoginToken
    public Result getUser(HttpServletRequest httpServletRequest) {
        UserVo userVo = new UserVo();
        
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
    
        BeanUtils.copyProperties(user, userVo);
    
        // 2. 获取获赞数、关注数、粉丝数
        Integer likesNumber = dynamicLikesService.getAllDynamicLikesCount(user.getId());
        userVo.setLikes(likesNumber);
        
        return ResultUtil.success(userVo);
    }
    
    // 获取用户其他信息：获赞数、关注数、粉丝数
    @GetMapping("/getInfo")
    @AccountLoginToken
    public Result getInfo(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 获取获赞数、关注数、粉丝数
        Integer likesNumber = dynamicLikesService.getAllDynamicLikesCount(user.getId());
    
        JSONObject result = new JSONObject();
        result.put("likesNumber", likesNumber);
        
        return ResultUtil.success(result);
    }
    
}
