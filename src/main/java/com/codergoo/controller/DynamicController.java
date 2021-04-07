package com.codergoo.controller;

import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Dynamic;
import com.codergoo.domain.User;
import com.codergoo.service.DynamicService;
import com.codergoo.service.TokenService;
import com.codergoo.vo.DynamicVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 动态模块接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@RestController
@RequestMapping("/api/dynamic")
@CrossOrigin
public class DynamicController {
    
    @Autowired
    public DynamicService dynamicService;
    
    @Autowired
    public TokenService tokenService;
    
    @PostMapping("/release")
    @AccountLoginToken
    public Result release(String content, Integer permissions, Integer type, Integer showAddress, String address, @RequestParam(name = "imageList", required = false) MultipartFile[] resourceList, HttpServletRequest httpServletRequest) {
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
        DynamicVo dynamicVo = dynamicService.addDynamic(dynamic, resourceList);
        
        return ResultUtil.success(200, "添加动态成功！", dynamicVo);
    }
    
    @PostMapping("/delete")
    @AccountLoginToken
    public Result delete(Integer id, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 移除根据id动态
        Boolean removeSuccess = dynamicService.removeDynamic(id, user.getId());
        
        if (removeSuccess) {
            return  ResultUtil.success(200, "删除动态成功！");
        }
        return ResultUtil.error(200, "删除动态失败！");
    }
    
    // 根据id获取动态
    @GetMapping("/getDynamic")
    public Result getDynamic(Integer id, HttpServletRequest httpServletRequest) {
        DynamicVo dynamicVo = dynamicService.getDynamicById(id);
        
        // 如果不是公开的，则需要校验用户
        if (1 != dynamicVo.getPermissions()) {
            // 根据 token 获取用户信息
            String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
            // 如果没用携带token
            if (null == token || "".equals(token)) {
                throw new RuntimeException("获取失败，该动态设置了查看权限！");
            }
            // 携带token，则需要判断用户是否是有权限看的
            User user = tokenService.getUserByToken(token);
            if (null == user || !dynamicVo.getUid().equals(user.getId())) {
                throw new RuntimeException("获取失败，该动态设置了查看权限！");
            }
        }
        return ResultUtil.success(dynamicVo);
    }
    
    // 获取自己的动态列表
    @GetMapping("/getSelfList")
    @AccountLoginToken
    public Result getSelfList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 根据uid查询动态列表
        List<DynamicVo> dynamicList = dynamicService.selfDynamicList(user.getId());
        return ResultUtil.success(dynamicList);
    }
    
    // 获取自己私密的动态列表
    @GetMapping("/getPrivateList")
    @AccountLoginToken
    public Result getPrivateList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
    
        return ResultUtil.success(dynamicService.listPrivateDynamic(user.getId()));
    }
    
    // 获取喜爱的动态列表
    @GetMapping("/getLikeList")
    @AccountLoginToken
    public Result getLikeList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        List<DynamicVo> dynamicList = dynamicService.listLikeDynamic(user.getId());
        return ResultUtil.success(dynamicList);
    }
}
