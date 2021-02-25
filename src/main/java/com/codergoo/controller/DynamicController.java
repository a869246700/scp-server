package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Dynamic;
import com.codergoo.domain.User;
import com.codergoo.service.DynamicService;
import com.codergoo.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 动态模块接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@RestController
@RequestMapping("/api/dynamic")
@CrossOrigin
@Slf4j
public class DynamicController {
    
    @Autowired
    public DynamicService dynamicService;
    
    @Autowired
    public TokenService tokenService;

    @GetMapping("/test")
    public Result test() {
        return ResultUtil.success(200,  "hello Dynamic！");
    }
    
    @PostMapping("/release")
    @AccountLoginToken
    public Result release(String content, Integer permissions, Integer type, Integer showAddress, String address, MultipartFile[] resourceList, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 初始化动态
        Dynamic dynamic = new Dynamic();
        dynamic.setUid(user.getId());
        dynamic.setContent(content);
        dynamic.setType(type == null ? 1 : type);
        dynamic.setShowAddress(showAddress == null ? 0 : 1);
        dynamic.setPermissions(permissions);
        dynamic.setAddress(address);

        // 3. 进行动态添加
        dynamic = dynamicService.addDynamic(dynamic, resourceList);
        
        return ResultUtil.success(200, "添加动态成功！", dynamic);
    }
}
