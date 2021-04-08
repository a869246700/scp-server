package com.codergoo.service.impl;

import com.codergoo.domain.*;
import com.codergoo.mapper.DynamicMapper;
import com.codergoo.mapper.DynamicResourceMapper;
import com.codergoo.service.DynamicLikesService;
import com.codergoo.service.DynamicResourceService;
import com.codergoo.service.DynamicService;
import com.codergoo.service.FriendService;
import com.codergoo.utils.RedisUtil;
import com.codergoo.vo.DynamicVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coderGoo
 * @date 2021/2/24
 */
@Service
@Slf4j
public class DynamicServiceImpl implements DynamicService {
    
    @Value("${web.upload.dynamic-resource}")
    public String dynamicResourceUpload;
    
    private String HOT_RANK_PREFIX = "dynamic_hot_rank:";
    
    private String LAST_THREE_DAY = "last_three_day";
    
    private String LAST_WEEK = "last_week";
    
    private String LAST_MONTH = "last_month";
    
    private SimpleDateFormat dynamicHotRankKeySdf = new SimpleDateFormat("yyyyMMdd");
    
    @Autowired
    public DynamicResourceService dynamicResourceService;
    
    @Autowired
    public DynamicLikesService dynamicLikesService;
    
    @Autowired
    public FriendService friendService;
    
    @Autowired
    public DynamicMapper dynamicMapper;
    
    @Autowired
    public DynamicResourceMapper dynamicResourceMapper;
    
    @Autowired
    public RedisUtil redisUtil;
    
    @Override
    public DynamicVo addDynamic(Dynamic dynamic, MultipartFile[] resourceList) {
        List<String> dynamicResourceList = new LinkedList<>(); // 保存的动态资源信息
        
        // 1. 获取id
        Integer id = this.getMaxId(); // 获取id
        // 2. 设置基础默认信息
        dynamic.setId(id); // 表单最大id
        dynamic.setStatus(1); // 默认：1
        dynamic.setTime(new Date(System.currentTimeMillis())); // 当前时间
        
        // 3. 保存动态信息
        dynamicMapper.addDynamic(dynamic);
        
        // 4. 判断保存路径是否为空
        File folder = new File(dynamicResourceUpload);
        // 目录判断, 如果路径不存在重新生成
        if (!folder.isDirectory()) {
            log.info("不存在目标路径，生成文件夹：" + dynamicResourceUpload);
            folder.mkdirs();
        }
        
        // 如果有传输资源的话
        if (resourceList != null && resourceList.length > 0) {
            // 5. 图片存储
            int index = 0; // 用于记录图片下标
            for (MultipartFile resource : resourceList) {
                // 生成动态资源文件的名称
                String oldFileName = resource.getOriginalFilename();
                // 用于控制空文件数组上传的情况
                if (null == oldFileName || "".equals(oldFileName)) {
                    break;
                }
                String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
                String fileName = "dynamic" + dynamic.getId() + "-resource-" + index + suffix; // 文件名称
                String filePath = dynamicResourceUpload + "/" + fileName;
                
                try {
                    // 6. 保存动态资源文件
                    resource.transferTo(new File(filePath));
                    
                    // 7. 将动态资源路径保存到 DynamicResource 表中
                    DynamicResource dynamicResource = new DynamicResource();
                    dynamicResource.setDid(dynamic.getId());
                    dynamicResource.setSrc(filePath);
                    dynamicResource.setType(1); // 默认为：1（图片）
                    Integer integer = dynamicResourceMapper.addDynamicResource(dynamicResource);
                    if (integer != 1) {
                        throw new RuntimeException("图片保存失败，请检查资源问题！");
                    }
                    dynamicResourceList.add(filePath);
                    index++; // 下标+1
                } catch (IOException e) {
                    throw new RuntimeException("图片保存失败，请检查资源问题！");
                }
            }
        }
        
        // 6. 给 dynamicVo 添加 dynamicResourceList
        DynamicVo dynamicVo = new DynamicVo();
        dynamicVo.setResourceList(dynamicResourceList);
        BeanUtils.copyProperties(dynamic, dynamicVo); // 属性转换
        
        // 7. 添加热度值
        Double hot = 10.0;
        this.addDynamicHot(dynamic.getId(), hot);
        dynamicVo.setHot(hot);
        
        // 8. 返回数据
        return dynamicVo;
    }
    
    @Override
    public Boolean removeDynamic(Integer id, Integer uid) {
        // 1. 判断动态是否属于该用户
        Integer hasDynamic = dynamicMapper.hasDynamic(uid, id);
        if (hasDynamic != 1) {
            log.error("hasDynamic: " + hasDynamic);
            throw new RuntimeException("数据存在异常，请联系开发人员紧急维护！");
        }
        
        // 2. 删除动态资源
        // 2.1 获取动态资源列表
        List<DynamicResource> dynamicResourceList = dynamicResourceMapper.listByDid(id);
        // 2.2 移除动态资源文件
        for (DynamicResource dynamicResource : dynamicResourceList) {
            File file = new File(dynamicResource.getSrc());
            // 判断文件是否存在
            if (file.exists()) {
                file.delete(); // 删除文件
            }
            // 从数据库只移除当前资源记录
            dynamicResourceMapper.removeDynamicResource(dynamicResource.getId());
        }
        log.info("dynamicResourceList: " + dynamicResourceList);
        // 3. 移除动态
        Integer delInteger = dynamicMapper.removeDynamic(id);
        return delInteger == 1;
    }
    
    @Override
    public Boolean updateDynamicPermissions(Integer id, Integer uid, Integer permissions) {
        // 1. 判断动态是否属于该用户
        Integer hasDynamic = dynamicMapper.hasDynamic(uid, id);
        if (hasDynamic != 1) {
            log.error("hasDynamic: " + hasDynamic);
            throw new RuntimeException("数据存在异常，请联系开发人员紧急维护！");
        }
        
        Integer integer = dynamicMapper.updateDynamicPermissions(id, permissions);
        return integer == 1;
    }
    
    @Override
    public List<DynamicVo> listDynamic() {
        // 1. 获取动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamic();
        
        // 2. 中间数据转换处理 PO => VO
        List<DynamicVo> resultDynamicList = new LinkedList<>();
        dynamicList.forEach(dynamic -> {
            DynamicVo dynamicVo = new DynamicVo();
            // 属性转换
            BeanUtils.copyProperties(dynamic, dynamicVo);
            
            // 动态资源
            List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
            dynamicVo.setResourceList(dynamicResourceList);
            // 动态评论
            dynamicVo.setDiscussesList(dynamic.getDiscussList());
            // 动态点赞
            List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
            dynamicVo.setLikesList(dynamicLikesList);
            
            resultDynamicList.add(dynamicVo);
        });
        
        // 3. 返回动态动态列表
        return resultDynamicList;
    }
    
    @Override
    public List<DynamicVo> listDynamicBySchool(Integer school) {
        // 1. 获取学校的动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamicBySchool(school);
        // 2. 数据转换
        return this.listTransfer(dynamicList);
    }
    
    @Override
    public List<DynamicVo> selfDynamicList(Integer uid) {
        // 1. 获取动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamicByUidAndPermissions(uid, 1);
        
        // 2. 中间数据转换处理 PO => VO
        List<DynamicVo> resultDynamicList = new LinkedList<>();
        dynamicList.forEach(dynamic -> {
            DynamicVo dynamicVo = new DynamicVo();
            // 属性转换
            BeanUtils.copyProperties(dynamic, dynamicVo);
            
            // 动态资源
            List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
            dynamicVo.setResourceList(dynamicResourceList);
            // 动态评论
            dynamicVo.setDiscussesList(dynamic.getDiscussList());
            // 动态点赞
            List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
            dynamicVo.setLikesList(dynamicLikesList);
            // 热度值
            dynamicVo.setHot(this.getHotById(dynamicVo.getId()));
            resultDynamicList.add(dynamicVo);
        });
        
        // 3. 返回动态动态列表
        return resultDynamicList;
    }
    
    @Override
    public List<DynamicVo> listPrivateDynamic(Integer uid) {
        // 1. 获取动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamicByUidAndPermissions(uid, 0);
        
        // 2. 中间数据转换处理 PO => VO
        List<DynamicVo> resultDynamicList = new LinkedList<>();
        dynamicList.forEach(dynamic -> {
            DynamicVo dynamicVo = new DynamicVo();
            // 属性转换
            BeanUtils.copyProperties(dynamic, dynamicVo);
            
            // 动态资源
            List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
            dynamicVo.setResourceList(dynamicResourceList);
            // 动态评论
            dynamicVo.setDiscussesList(dynamic.getDiscussList());
            // 动态点赞
            List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
            dynamicVo.setLikesList(dynamicLikesList);
            // 热度值
            dynamicVo.setHot(this.getHotById(dynamicVo.getId()));
            resultDynamicList.add(dynamicVo);
        });
        
        // 3. 返回动态动态列表
        return resultDynamicList;
    }
    
    @Override
    public List<DynamicVo> listLikeDynamic(Integer uid) {
        // 1. 根据uid获取用户的点赞列表
        List<DynamicLikes> dynamicLikes = dynamicLikesService.getDynamicLikeList(uid);
        // 2. 遍历喜欢列表获取动态信息
        List<DynamicVo> dynamicVoList = new LinkedList<>();
        dynamicLikes.forEach(like -> dynamicVoList.add(this.getDynamicById(like.getDid())));
        return dynamicVoList;
    }
    
    @Override
    public List<DynamicVo> listFriendDynamic(Integer uid) {
        // 1. 获取好友的动态列表
        List<Dynamic> dynamicList = dynamicMapper.listFriendDynamicByUid(uid);
        
        // 2. 数据转换
        return this.listTransfer(dynamicList);
    }
    
    @Override
    public List<DynamicVo> listAttentionDynamic(Integer uid) {
        // 1. 获取好友的动态列表
        List<Dynamic> dynamicList = dynamicMapper.listAttentionDynamicByUid(uid);
    
        // 2. 数据转换
        return this.listTransfer(dynamicList);
    }
    
    @Override
    public List<DynamicVo> listDynamicByAddress(String address) {
        // 1. 根据地址获取动态列表
        List<Dynamic> dynamicList = dynamicMapper.listDynamicByAddress(address);
        return listTransfer(dynamicList);
    }
    
    public DynamicVo transfer(Dynamic dynamic) {
        DynamicVo dynamicVo = new DynamicVo();
        // 属性转换
        BeanUtils.copyProperties(dynamic, dynamicVo);
        // 动态资源转换
        List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
        dynamicVo.setResourceList(dynamicResourceList);
        // 动态评论
        dynamicVo.setDiscussesList(dynamic.getDiscussList());
        // 动态点赞
        List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
        dynamicVo.setLikesList(dynamicLikesList);
        // 动态热度
        dynamicVo.setHot(this.getHotById(dynamicVo.getId()));
        
        return dynamicVo;
    }
    
    public List<DynamicVo> listTransfer(List<Dynamic> dynamicList) {
        // 2. 中间数据转换处理 PO => VO
        List<DynamicVo> resultDynamicList = new LinkedList<>();
        dynamicList.forEach(dynamic -> {
            DynamicVo dynamicVo = new DynamicVo();
            // 属性转换
            BeanUtils.copyProperties(dynamic, dynamicVo);
        
            // 动态资源
            List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
            dynamicVo.setResourceList(dynamicResourceList);
            // 动态评论
            dynamicVo.setDiscussesList(dynamic.getDiscussList());
            // 动态点赞
            List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
            dynamicVo.setLikesList(dynamicLikesList);
        
            resultDynamicList.add(dynamicVo);
        });
    
        // 3. 返回动态动态列表
        return resultDynamicList;
    }
    
    @Override
    public List<Dynamic> selfAllDynamicList(Integer uid) {
        return dynamicMapper.listDynamicByUid(uid);
    }
    
    @Override
    public Integer getMaxId() {
        Integer id = dynamicMapper.getMaxId();
        return id == null ? 1 : id + 1;
    }
    
    @Override
    public DynamicVo getDynamicById(Integer id) {
        Dynamic dynamic = dynamicMapper.findById(id);
        if (null == dynamic) {
            return null;
        }
        
        DynamicVo dynamicVo = new DynamicVo();
        // 属性转换
        BeanUtils.copyProperties(dynamic, dynamicVo);
        // 动态资源转换
        List<String> dynamicResourceList = dynamic.getResourceList().stream().map(DynamicResource::getSrc).collect(Collectors.toList());
        dynamicVo.setResourceList(dynamicResourceList);
        // 动态评论
        dynamicVo.setDiscussesList(dynamic.getDiscussList());
        // 动态点赞
        List<User> dynamicLikesList = dynamic.getLikesList().stream().map(DynamicLikes::getUser).collect(Collectors.toList());
        dynamicVo.setLikesList(dynamicLikesList);
        // 动态热度
        dynamicVo.setHot(this.getHotById(dynamicVo.getId()));
        
        return dynamicVo;
    }
    
    @Override
    public Double getHotById(Integer id) {
        // 1. 获取今日热度值排行榜
        Date today = new Date(System.currentTimeMillis());
        String todayStr = dynamicHotRankKeySdf.format(today);
        String key = HOT_RANK_PREFIX + todayStr;
        Set<TypedTuple<String>> todayHotRank = redisUtil.zReverseRangeWithScores(key, 0, -1);
        // 2. 根据id获取该动态的热度值
        Optional<TypedTuple<String>> first = todayHotRank.stream().filter(item -> Objects.equals(item.getValue(), String.valueOf(id))).findFirst();
        if (first.isPresent()) {
            // 存在
            TypedTuple<String> dynamicRank = first.get();
            return dynamicRank.getScore();
        } else {
            return 0.0;
        }
    }
    
    @Override
    public List<DynamicVo> getLastMonthDayHotRank(Integer rankLength) {
        return this.getHotRankByDateStr(LAST_MONTH, rankLength);
    }
    
    @Override
    public List<DynamicVo> getLastWeekHotRank(Integer rankLength) {
        return this.getHotRankByDateStr(LAST_WEEK, rankLength);
    }
    
    @Override
    public List<DynamicVo> getLastThreeDayHotRank(Integer rankLength) {
        return this.getHotRankByDateStr(LAST_THREE_DAY, rankLength);
    }
    
    @Override
    public List<DynamicVo> getYesterdayHotRank(Integer rankLength) {
        Date yesterday = DateUtils.addDays(new Date(System.currentTimeMillis()), -1);
        String yesterdayStr = dynamicHotRankKeySdf.format(yesterday);
        
        return this.getHotRankByDateStr(yesterdayStr, rankLength);
    }
    
    @Override
    public List<DynamicVo> getTodayHotRank(Integer rankLength) {
        Date today = new Date(System.currentTimeMillis());
        String todayStr = dynamicHotRankKeySdf.format(today);
        
        return this.getHotRankByDateStr(todayStr, rankLength);
    }
    
    @Override
    public List<DynamicVo> getHotRankByDateStr(String dateStr, Integer rankLength) {
        List<DynamicVo> dynamicVoList = new LinkedList<>();
        
        // 获取key
        String key = HOT_RANK_PREFIX + dateStr;
        
        // 根据key获取数据
        List<String> rankList = redisUtil.zReverseRangeWithScores(key, 0, rankLength).stream().map(TypedTuple::getValue).collect(Collectors.toList());
        List<Double> scoreList = redisUtil.zReverseRangeWithScores(key, 0, rankLength).stream().map(TypedTuple::getScore).collect(Collectors.toList());
        
        for (int i = 0; i < rankList.size(); i++) {
            Integer id = Integer.valueOf(rankList.get(i)); // 获取id
            Double hot = scoreList.get(i); // 获取热度值
            
            DynamicVo dynamicVo = this.getDynamicById(id);
            // 如果为空，跳过
            if (null == dynamicVo) {
                continue;
            }
            // 如果设置权限跳过
            if (1 != dynamicVo.getPermissions()) {
                continue;
            }
            dynamicVo.setHot(hot); // 设置热度值
            
            dynamicVoList.add(dynamicVo);
        }
        
        return dynamicVoList;
    }
    
    @Override
    public Boolean calcLastMonthHotRank() {
        try {
            // 获取 lastMonthKey
            List<String> lastMonthKey = this.getLastHotRankKey(30);
            redisUtil.zUnionAndStore(null, lastMonthKey, HOT_RANK_PREFIX + LAST_MONTH);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean calcLastWeekHotRank() {
        try {
            // 获取 lastWeekKey
            List<String> lastWeekKey = this.getLastHotRankKey(7);
            redisUtil.zUnionAndStore(null, lastWeekKey, HOT_RANK_PREFIX + LAST_WEEK);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean calcLastThreeDayHotRank() {
        try {
            // 获取 lastThreeDayKey
            List<String> lastThreeDayKey = this.getLastHotRankKey(3);
            redisUtil.zUnionAndStore(null, lastThreeDayKey, HOT_RANK_PREFIX + LAST_THREE_DAY);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<String> getLastHotRankKey(Integer days) {
        List<String> keyList = new LinkedList<>();
        for (int day = 1; day <= days; day++) {
            Date date = DateUtils.addDays(new Date(System.currentTimeMillis()), -day);
            String key = HOT_RANK_PREFIX + dynamicHotRankKeySdf.format(date);
            keyList.add(key);
        }
        return keyList;
    }
    
    @Override
    public Boolean addDynamicHot(Integer id, Double hotValue) {
        if (id == null) {
            return false;
        }
        try {
            // 只给当天热度添加
            Date today = new Date(System.currentTimeMillis());
            String todayStr = HOT_RANK_PREFIX + dynamicHotRankKeySdf.format(today);
            
            redisUtil.zIncrementScore(todayStr, String.valueOf(id), hotValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
