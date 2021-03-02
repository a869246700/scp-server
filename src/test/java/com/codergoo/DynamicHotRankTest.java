package com.codergoo;

import com.codergoo.domain.Dynamic;
import com.codergoo.service.DynamicService;
import com.codergoo.service.impl.DynamicServiceImpl;
import com.codergoo.utils.RedisUtil;
import com.codergoo.vo.DynamicVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.AccessLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author coderGoo
 * @date 2021/2/27
 */
@SpringBootTest
@Slf4j
public class DynamicHotRankTest {
    
    @Autowired
    public DynamicService dynamicService;
    
    private String HOT_RANK_PREFIX = "dynamic_hot_rank:";
    
    @Autowired
    public RedisUtil redisUtil;
    
    // 测试生成动态热度，已有的动态为标准10条（数据库中获取）
    @Test
    void testGenerateTodayHotRank() {
        Date today = new Date(System.currentTimeMillis()); // 今天
        SimpleDateFormat keySdf = new SimpleDateFormat("yyyyMMdd");
    
        String todayKey = HOT_RANK_PREFIX + keySdf.format(today);
        List<DynamicVo> dynamicVoList = dynamicService.listDynamic();
        List<Integer> idList = dynamicVoList.stream().map(DynamicVo::getId).collect(Collectors.toList());
    
        for (Integer integer : idList) {
            String id = String.valueOf(integer);
            // 设置zSet
            // 初始化数据
            int randomVal = Integer.parseInt(RandomStringUtils.randomNumeric(4));
            redisUtil.zAdd(todayKey, id, randomVal);
        }
        log.info("添加成功");
        log.info("todayKey: " + todayKey);
        log.info("idList: " + idList);
    }
    
    // 模拟生成前30天的数据
    @Test
    void testGenerateLastMonthHotRank() {
        List<String> keyList = dynamicService.getLastHotRankKey(30);
        
        // 获取数据库前十条数据
        List<DynamicVo> dynamicVoList = dynamicService.listDynamic();
        List<Integer> idList = dynamicVoList.stream().map(DynamicVo::getId).collect(Collectors.toList());
        
        // 生成每日数据
        keyList.forEach(String -> {
            log.info(String);
    
            for (Integer integer : idList) {
                String id = integer.toString();
                // 设置zSet
                // 初始化数据
                int randomVal = Integer.parseInt(RandomStringUtils.randomNumeric(4));
                redisUtil.zAdd(String, id, randomVal);
            }
        });
    }
    
    // 测试获取今天热度排行前十
    @Test
    void testGetTodayHotRank() {
        List<DynamicVo> dynamicVoList = dynamicService.getTodayHotRank(10);
    
        log.info("todayHotRankLength: " + dynamicVoList.size());
        dynamicVoList.forEach(DynamicVo -> log.info(DynamicVo.toString()));
    }
    
    // 测试获取昨天热度排行前十
    @Test
    void testGetYesterdayHotRank() {
        List<DynamicVo> dynamicVoList = dynamicService.getYesterdayHotRank(10);
        log.info("dynamicVoList:" + dynamicVoList);
        log.info("yesterdayHotRankLength: " + dynamicVoList.size());
        dynamicVoList.forEach(DynamicVo -> log.info(DynamicVo.toString()));
    }
    
    // 测试计算前三天热度排行
    @Test
    void testCalcLastThreeDayHotRank() {
        dynamicService.calcLastThreeDayHotRank();
    }
    
    // 测试获取前三天热度排行前十
    @Test
    void testGetLastThreeDayHotRank() {
        List<DynamicVo> dynamicVoList = dynamicService.getLastThreeDayHotRank(10);
        
        log.info("lastThreeDayHotRankLength: " + dynamicVoList.size());
        dynamicVoList.forEach(DynamicVo -> log.info(DynamicVo.toString()));
    }
    
    // 测试计算前七天（week）热度排行
    @Test
    void testCalcLastWeekHotRank() {
        dynamicService.calcLastWeekHotRank();
    }
    
    // 测试获取前七天（week）热度排行前十
    @Test
    void testGetLastWeekHotRank() {
        List<DynamicVo> dynamicVoList = dynamicService.getLastWeekHotRank(10);
        
        log.info("lastWeekHotRankLength: " + dynamicVoList.size());
        dynamicVoList.forEach(DynamicVo -> log.info(DynamicVo.toString()));
    }
    
    // 测试计算前三十天（month）热度排行
    @Test
    void testCalcLastMonthHotRank() {
        dynamicService.calcLastMonthHotRank();
    }
    
    // 测试获取前三十天（month）热度排行前十
    @Test
    void testGetLastMonthHotRank() {
        List<DynamicVo> dynamicVoList = dynamicService.getLastMonthDayHotRank(10);
        
        log.info("lastMonthHotRankLength: " + dynamicVoList.size());
        dynamicVoList.forEach(DynamicVo -> log.info(DynamicVo.toString()));
    }
}
