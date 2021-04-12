package com.codergoo.service.impl;

import com.codergoo.service.DynamicHotRankService;
import com.codergoo.service.DynamicService;
import com.codergoo.utils.RedisUtil;
import com.codergoo.vo.DynamicVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author coderGoo
 * @date 2021/3/24
 */
@Service
public class DynamicHotRankServiceImpl implements DynamicHotRankService {
    
    @Autowired
    public DynamicService dynamicService;
    
    @Autowired
    public RedisUtil redisUtil;
    
    private String HOT_RANK_PREFIX = "dynamic_hot_rank:";
    
    private SimpleDateFormat dynamicHotRankKeySdf = new SimpleDateFormat("yyyyMMdd");
    
    @Override
    public Boolean generateTodayHotRank() {
        Date today = new Date(System.currentTimeMillis()); // 今天
    
        String todayKey = HOT_RANK_PREFIX + dynamicHotRankKeySdf.format(today);
        List<DynamicVo> dynamicVoList = dynamicService.listDynamic();
        List<Integer> idList = dynamicVoList.stream().map(DynamicVo::getId).collect(Collectors.toList());
    
        for (Integer integer : idList) {
            String id = String.valueOf(integer);
            // 设置zSet
            // 初始化数据
            int randomVal = Integer.parseInt(RandomStringUtils.randomNumeric(4));
            redisUtil.zAdd(todayKey, id, randomVal);
        }
        return true;
    }
}
