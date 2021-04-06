package com.codergoo.mapper;

import com.codergoo.domain.Dynamic;
import com.codergoo.domain.DynamicDiscuss;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 动态评论持久层
 *
 * @author coderGoo
 * @date 2021/2/25
 */
@Mapper
@Repository
public interface DynamicDiscussMapper {
    
    // 添加评论
    @Insert({
            "insert into scp_dynamic_discuss(",
            "id, uid, did, content, pid, time",
            ")",
            "values(",
            "#{id}, #{uid}, #{did}, #{content}, #{pid}, #{time}",
            ")"
    })
    Integer addDiscuss(DynamicDiscuss dynamicDiscuss);
    
    @Select("select max(id) from scp_dynamic_discuss")
    Integer getMaxId();
    
    // 删除评论
    @Delete("delete from scp_dynamic_discuss where id = #{id}")
    Integer removeDiscuss(Integer id);
    
    @Select("select * from scp_dynamic_discuss where id = #{id}")
    @Results({
            @Result(column = "id", property = "id", id = true, jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "children", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.findChildrenDiscussByPid"))
    })
    DynamicDiscuss findById(Integer id);
    
    // 根据评论id获取children的评论列表
    @Select("select * from scp_dynamic_discuss where pid = #{pid} and pid is not null")
    @Results({
            @Result(column = "id", property = "id", id = true, jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "children", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.findChildrenDiscussByPid"))
    })
    List<DynamicDiscuss> findChildrenDiscussByPid(Integer pid);
    
    // 根据动态did获取评论列表
    @Select("select * from scp_dynamic_discuss where did = #{did} and pid is null")
    @Results({
            @Result(column = "id", property = "id", id = true, jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "children", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.findChildrenDiscussByPid"))
    })
    List<DynamicDiscuss> listByDid(Integer did);
}
