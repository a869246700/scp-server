package com.codergoo.controller;

import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态热度排行接口
 * 
 * @author coderGoo
 * @date 2021/2/27
 */
@RestController
@RequestMapping("/api/dynamicHotRank")
@CrossOrigin
public class DynamicHotRankController {
    
    @Autowired
    public DynamicService dynamicService;
    
    @GetMapping("/test")
    public Result test() {
        return ResultUtil.success(200, "test");
    }
    
    // 获取今天热度排行前十
    @GetMapping("/getTodayHotRank")
    public Result getTodayHotRank() {
        return ResultUtil.success(dynamicService.getTodayHotRank(10));
    }
    
    // 获取昨天热度排行前十
    @GetMapping("/getYesterdayHotRank")
    public Result getYesterdayHotRank() {
        return ResultUtil.success(dynamicService.getYesterdayHotRank(10));
    }
    
    // 获取前三天热度排行前十
    @GetMapping("/getLastThreeDayHotRank")
    public Result getLastThreeDayHotRank() {
        return ResultUtil.success(dynamicService.getLastThreeDayHotRank(10));
    }
    
    // 获取前七天（week）热度排行前十
    @GetMapping("/getLastWeekHotRank")
    public Result getLastWeekHotRank() {
        return ResultUtil.success(dynamicService.getLastWeekHotRank(10));
    }
    
    // 获取前三十天（month）热度排行前十
    @GetMapping("/getLastMonthHotRank")
    public Result getLastMonthHotRank() {
        return ResultUtil.success(dynamicService.getLastMonthDayHotRank(10));
    }
}
