package com.codergoo.scheduled;

import com.codergoo.service.DynamicHotRankService;
import com.codergoo.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 动态热榜模块的定时任务
 *
 * @author coderGoo
 * @date 2021/2/27
 */
@Component
@Slf4j
public class DynamicHotRankScheduled {
    
    @Autowired
    public DynamicService dynamicService;
    
    @Autowired
    public DynamicHotRankService dynamicHotRankService;
    
    @Scheduled(cron = "0 */30 * * * ?")
    public void test() {
        // 随机生成今日热榜数据
        log.info("开始随机生成今日热榜开始！");
        dynamicHotRankService.generateTodayHotRank();
        log.info("开始随机生成今日热榜完成！");
    }
    
    // 每天 0 点计算排行榜数据
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void calcHotRank() {
        log.info("计算 前三天、前七天、前三十天 动态热榜任务开始！");
        // 分别计算前三天、前七天、前三十天 动态热榜
        dynamicService.calcLastThreeDayHotRank();
        dynamicService.calcLastWeekHotRank();
        dynamicService.calcLastMonthHotRank();
        log.info("计算 前三天、前七天、前三十天 动态热榜任务完成！");
    }
}
