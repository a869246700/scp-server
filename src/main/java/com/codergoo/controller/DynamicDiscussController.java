package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.DynamicDiscuss;
import com.codergoo.domain.User;
import com.codergoo.mapper.DynamicDiscussMapper;
import com.codergoo.service.DynamicDiscussService;
import com.codergoo.service.DynamicResourceService;
import com.codergoo.service.TokenService;
import com.codergoo.vo.DynamicDiscussVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 动态评论接口
 *
 * @author coderGoo
 * @date 2021/2/25
 */
@Slf4j
@RestController
@RequestMapping("/api/dynamicDiscuss")
@CrossOrigin
public class DynamicDiscussController {
    
    @Autowired
    public TokenService tokenService;
    
    @Autowired
    public DynamicDiscussService dynamicDiscussService;
    
    // 添加评论
    @PostMapping("/addDynamicDiscuss")
    @AccountLoginToken
    public Result addDynamicDiscuss(@RequestBody DynamicDiscussVo dynamicDiscussVo, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        if (null == dynamicDiscussVo.getDid()) {
            return ResultUtil.error(200, "请携带动态id！");
        }
        
        // 2. 数据获取
        DynamicDiscuss dynamicDiscuss = new DynamicDiscuss();
        BeanUtils.copyProperties(dynamicDiscussVo, dynamicDiscuss);
        dynamicDiscuss.setUid(user.getId());
        
        log.info("dynamicDiscuss:" + dynamicDiscuss);
        
        // 3. 添加动态评论
        DynamicDiscuss addSuccess = dynamicDiscussService.addDynamicDiscuss(dynamicDiscuss);
        
        return null != addSuccess ? ResultUtil.success(200, "评论成功！", addSuccess) : ResultUtil.error(200, "评论失败！");
    }
    
    // 删除评论
    @PostMapping("/deleteDynamicDiscuss")
    @AccountLoginToken
    public Result deleteDynamicDiscuss(Integer id, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        if (null == id) {
            return ResultUtil.error(200, "错误操作，请输入评论Id！");
        }
        
        Boolean removeSuccess = dynamicDiscussService.removeDynamicDiscuss(id, user.getId());
        return removeSuccess ? ResultUtil.success(200, "删除评论成功！") : ResultUtil.error(200, "删除评论失败！");
    }
}
