package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天控制层
 *
 * @author coderGoo
 * @date 2021/4/13
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {
    
    @Autowired
    public ChatService chatService;
    
    // 获取所有聊天记录
    @PostMapping("/getChatRecord")
    @AccountLoginToken
    public Result getChatRecord(Integer uid, Integer fid) {
        return ResultUtil.success(chatService.getChatRecord(uid, fid));
    }
    
    // 删除单个聊天记录
    @PostMapping("/delChatRecordRow")
    @AccountLoginToken
    public Result delChatRecordRow(Integer id) {
        Boolean success = chatService.delChatRecordRow(id);
        return success ? ResultUtil.success(200, "删除成功!") : ResultUtil.error(500, "删除失败!    ");
    }
}
