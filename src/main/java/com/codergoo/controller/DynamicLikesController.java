package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.User;
import com.codergoo.service.DynamicLikesService;
import com.codergoo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 动态点赞模块控制层
 *
 * @author coderGoo
 * @date 2021/2/26
 */
@RestController
@RequestMapping("/api/dynamicLikes")
@CrossOrigin
public class DynamicLikesController {
    
    @Autowired
    public DynamicLikesService dynamicLikesService;
    
    @Autowired
    public TokenService tokenService;
    
    @PostMapping("/giveLikes")
    @AccountLoginToken
    public Result giveLike(Integer did, HttpServletRequest httpServletRequest) {
        if (null == did) {
            return ResultUtil.error(200, "请求失败，请携带动态ID！");
        }
        
        // 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        Boolean addSuccess = dynamicLikesService.addLikes(user.getId(), did);
        return addSuccess ? ResultUtil.success(200, "点赞成功！") : ResultUtil.error(200, "点赞失败！");
    }
    
    @PostMapping("/cancelLikes")
    @AccountLoginToken
    public Result cancelLike(Integer did, HttpServletRequest httpServletRequest) {
        if (null == did) {
            return ResultUtil.error(200, "请求失败，请携带动态ID！");
        }
    
        // 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
    
        Boolean removeSuccess = dynamicLikesService.removeLikes(user.getId(), did);
        return removeSuccess ? ResultUtil.success(200, "取消成功！") : ResultUtil.error(200, "取消失败！");
    }
}
