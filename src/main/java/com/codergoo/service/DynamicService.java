package com.codergoo.service;

import com.codergoo.domain.Dynamic;
import com.codergoo.vo.DynamicVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 动态模块业务层接口
 *
 * @author coderGoo
 * @date 2021/2/24
 */
public interface DynamicService {
    
    List<DynamicVo> searchDynamicByKeyword(String key);
    
    List<Dynamic> selfAllDynamicList(Integer uid);
    
    // 根据动态id获取用户id
    Integer getUidByDid(Integer did);
    
    // 添加动态
    DynamicVo addDynamic(Dynamic dynamic, MultipartFile[] resourceList);
    
    // 删除动态
    Boolean removeDynamic(Integer id, Integer uid);
    
    // 根据学校id获取动态列表
    List<DynamicVo> listDynamicBySchool(Integer school);
    
    // 获取所有动态列表（用于测试）
    List<DynamicVo> listDynamic();
    
    // 获取自己的动态列表
    List<DynamicVo> selfDynamicList(Integer uid);
    
    // 根据用户id获取私密动态列表
    List<DynamicVo> listPrivateDynamic(Integer uid);
    
    // 获取喜爱的列表
    List<DynamicVo> listLikeDynamic(Integer uid);
    
    // 获取好友的动态列表
    List<DynamicVo> listFriendDynamic(Integer uid);
    
    // 获取关注的动态列表
    List<DynamicVo> listAttentionDynamic(Integer uid);
    
    // 根据地址模糊查询动态列表
    List<DynamicVo> listDynamicByAddress(String address);
    
    // 获取动态最大ID
    Integer getMaxId();
    
    // 修改动态的查看权限
    Boolean updateDynamicPermissions(Integer id, Integer uid, Integer permissions);
    
    // 根据动态id获取动态
    DynamicVo getDynamicById(Integer id);
    
    // 根据id获取热度
    Double getHotById(Integer id);
    
    // 计算近一个月排行
    Boolean calcLastMonthHotRank();
    
    // 计算近一周排行
    Boolean calcLastWeekHotRank();
    
    // 计算近三天排行
    Boolean calcLastThreeDayHotRank();
    
    // 计算过去热度排行
    List<String> getLastHotRankKey(Integer days);
    
    // 获取近一月热度排行
    List<DynamicVo> getLastMonthDayHotRank(Integer rankLength);
    
    // 获取近一周热度排行
    List<DynamicVo> getLastWeekHotRank(Integer rankLength);
    
    // 获取近三天热度排行
    List<DynamicVo> getLastThreeDayHotRank(Integer rankLength);
    
    // 获取昨日热度排行
    List<DynamicVo> getYesterdayHotRank(Integer rankLength);
    
    // 获取今日热度排行
    List<DynamicVo> getTodayHotRank(Integer rankLength);
    
    // 根据日期获取热度排行
    List<DynamicVo> getHotRankByDateStr(String date, Integer rankLength);
    
    // 添加热度
    Boolean addDynamicHot(Integer id, Double hotValue);
}
