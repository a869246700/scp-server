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
            "uid, did, content, rid, time",
            ")",
            "values(",
            "#{uid}, #{did}, #{content}, #{rid}, #{time}",
            ")"
    })
    Integer addDiscuss(DynamicDiscuss dynamicDiscuss);
    
    // 删除评论
    @Delete("delete from scp_dynamic_discuss where id = #{id}")
    Integer removeDiscuss(Integer id);
    
    @Select("select * from scp_dynamic_discuss where id = #{id}")
    DynamicDiscuss findById(Integer id);
    
    // 根据动态did获取评论列表
    @Select("select * from scp_dynamic_discuss where did = #{did}")
    @Results({
            @Result(column = "id", property = "id", id = true, jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "rid", property = "rUser", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    List<DynamicDiscuss> listByDid(Integer did);
}
