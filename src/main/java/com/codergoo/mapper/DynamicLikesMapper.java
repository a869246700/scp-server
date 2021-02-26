package com.codergoo.mapper;

import com.codergoo.domain.DynamicLikes;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 动态点赞持久层
 *
 * @author coderGoo
 * @date 2021/2/26
 */
@Mapper
@Repository
public interface DynamicLikesMapper {
    
    @Select("select * from scp_dynamic_likes where did = #{did} and uid = #{uid}")
    DynamicLikes findByDidAndUid(Integer did, Integer uid);
    
    @Select("select * from scp_dynamic_likes where id = #{id}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    DynamicLikes findById(Integer id);
    
    @Select("select * from scp_dynamic_likes where did = #{did}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    List<DynamicLikes> listByDid(Integer did);
    
    @Select("select * from scp_dynamic_likes where uid = #{uid}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById"))
    })
    List<DynamicLikes> listByUid(Integer uid);
    
    @Insert({
            "insert into scp_dynamic_likes(",
            "uid, did",
            ")",
            "values(",
            "#{uid}, #{did}",
            ")"
    })
    Integer addLikes(DynamicLikes dynamicLikes);
    
    @Delete("delete from scp_dynamic_likes where id = #{id}")
    Integer removeLikes(Integer id);
}
