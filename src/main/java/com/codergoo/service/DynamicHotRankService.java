package com.codergoo.service;

/**
 * 动态热榜
 *
 * @author coderGoo
 * @date 2021/3/24
 */
public interface DynamicHotRankService {
    
    // 生成今天的排行榜
    Boolean generateTodayHotRank();
}
