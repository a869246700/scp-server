package com.codergoo.mapper;

import com.codergoo.domain.Friend;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 好友持久层
 *
 * @author coderGoo
 * @date 2021/4/7
 */
@Repository
@Mapper
public interface FriendMapper {
    
    // 查询好友
    @Select("select a.* from scp_friend a, scp_friend b where b.uid = a.fid and b.fid = a.uid and a.uid = #{uid}")
    @Results({
            @Result(column = "fid", property = "fid", jdbcType = JdbcType.INTEGER),
            @Result(column = "fid", property = "friend", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    List<Friend> listFriend(Integer uid);
    
    // 查询关注
    @Select("select * from scp_friend where uid = #{uid}")
    @Results({
            @Result(column = "fid", property = "fid", jdbcType = JdbcType.INTEGER),
            @Result(column = "fid", property = "friend", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    List<Friend> listAttention(Integer uid);
    
    // 查询关注数量
    @Select("select count(*) from scp_friend where uid = #{uid}")
    Integer getAttentionNumber(Integer uid);
    
    // 查询粉丝数量
    @Select("select count(*) from scp_friend where fid = #{uid}")
    Integer getFansNumber(Integer uid);
    
    // 添加关注
    @Insert({
            "insert into",
            "scp_friend(",
            "fid, remark, status, uid, gid",
            ") values(",
            "#{fid}, #{remark}, #{status}, #{uid}, #{gid}",
            ")"
    })
    Integer addAttention(Friend friend);
    
    // 移除关注
    @Delete("delete from scp_friend where uid = #{uid} and fid = #{fid}")
    Integer removeAttention(Integer fid, Integer uid);
}
