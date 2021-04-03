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
    public Result getDynamic(Integer id) {
        DynamicVo dynamicVo = dynamicService.getDynamicById(id);
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
    
    // 获取喜爱的动态列表
    @GetMapping("/getLikeList")
    @AccountLoginToken
    public Result getLikeList(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        List<DynamicVo> dynamicList = dynamicService.likeDynamicList(user.getId());
        return ResultUtil.success(dynamicList);
    }
    // 获取今日热榜
    @GetMapping("/getTodayHotRank")
    public Result getTodayHotRank() {
        List<DynamicVo> todayHotRank = dynamicService.getTodayHotRank(10);
        return ResultUtil.success(todayHotRank);
    }
    
    // 获取昨日热榜
    @GetMapping("/getYesterdayHotRank")
    public Result getYesterdayHotRank() {
        List<DynamicVo> todayHotRank = dynamicService.getYesterdayHotRank(10);
        return ResultUtil.success(todayHotRank);
    }
    
    // 获取前三天热榜
    @GetMapping("/getLastThreeDayHotRank")
    public Result getLastThreeDayHotRank() {
        List<DynamicVo> todayHotRank = dynamicService.getLastThreeDayHotRank(10);
        return ResultUtil.success(todayHotRank);
    }
    
    // 获取前七天热榜
    @GetMapping("/getLastWeekHotRank")
    public Result getLastWeekHotRank() {
        List<DynamicVo> todayHotRank = dynamicService.getLastWeekHotRank(10);
        return ResultUtil.success(todayHotRank);
    }
    
    // 获取前三十天热榜
    @GetMapping("/getLastMonthHotRank")
    public Result getLastMonthHotRank() {
        List<DynamicVo> todayHotRank = dynamicService.getLastMonthDayHotRank(10);
        return ResultUtil.success(todayHotRank);
    }
    
    // 修改动态的权限
    @PostMapping("/updatePermissions")
    public Result updatePermissions(Integer id, Integer permissions, HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token"); // 从 http 请求头中取出 token
        User user = tokenService.getUserByToken(token);
        
        // 2. 判断permissions
        if (null == permissions) {
            return ResultUtil.error(200, "请求失败，请选择正确的查看权限！");
        }
        
        // 3. 修改权限
        Boolean updateSuccess = dynamicService.updateDynamicPermissions(id, user.getId(), permissions);
        if (updateSuccess) {
            return ResultUtil.success(200, "修改查看权限成功");
        }
        return ResultUtil.error(200, "修改查看权限失败!");
    }
}
