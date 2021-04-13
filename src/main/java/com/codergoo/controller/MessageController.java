package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.User;
import com.codergoo.service.MessageService;
import com.codergoo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 消息通知接口
 *
 * @author coderGoo
 * @date 2021/4/13
 */
@RestController
@RequestMapping("/api/message")
@CrossOrigin
public class MessageController {
    
    @Autowired
    public TokenService tokenService;
    
    @Autowired
    public MessageService messageService;
    
    // 获取我的消息列表
    @GetMapping("/getMessageList")
    @AccountLoginToken
    public Result getMessageList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        return ResultUtil.success(messageService.getMessage(user.getId()));
    }
    
    // 删除消息通知
    @PostMapping("/delMessage")
    @AccountLoginToken
    public Result delMessage(Integer id) {
        Boolean success = messageService.delMessageRow(id);
        return success ? ResultUtil.success(200, "删除成功！") : ResultUtil.error(500, "删除失败！");
    }
}
