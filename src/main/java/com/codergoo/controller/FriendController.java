package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Friend;
import com.codergoo.domain.User;
import com.codergoo.service.FriendService;
import com.codergoo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 好友接口
 *
 * @author coderGoo
 * @date 2021/4/7
 */
@RestController
@RequestMapping("/api/friend")
@CrossOrigin
public class FriendController {
    
    @Autowired
    public FriendService friendService;
    
    @Autowired
    public TokenService tokenService;
    
    // 获取好友列表
    @GetMapping("/list")
    @AccountLoginToken
    public Result getFriendList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 获取好友列表
        List<Friend> friendList = friendService.listFriend(user.getId());
        return ResultUtil.success(friendList);
    }
    
    // 获取关注列表
    @GetMapping("/getAttentionList")
    @AccountLoginToken
    public Result getAttentionList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 获取关注列表
        List<Friend> friendList = friendService.listAttention(user.getId());
        return ResultUtil.success(friendList);
    }
    
    // 添加关注
    @PostMapping("/addAttention")
    @AccountLoginToken
    public Result addAttention(Integer fid, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        Boolean success = friendService.addAttention(fid, user.getId());
        return success ? ResultUtil.success(200, "添加关注成功！") : ResultUtil.error(500, "添加关注失败！");
    }
    
    // 移除关注
    @PostMapping("/cancelAttention")
    @AccountLoginToken
    public Result cancelAttention(Integer fid, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
    
        Boolean success = friendService.cancelAttention(fid, user.getId());
        return success ? ResultUtil.success(200, "取消关注成功！") : ResultUtil.error(500, "取消关注失败！");
    }
}
