package com.codergoo.mapper;

import com.codergoo.domain.Dynamic;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 动态持久层
 *
 * @author coderGoo
 * @date 2021/2/24
 */
@Mapper
@Repository
public interface DynamicMapper {
    
    // 添加动态
    @Insert({
            "insert into scp_dynamic(",
            "id, uid, content, type, camouflage, tag, permissions, status, address, show_address, time",
            ")",
            "values(",
            "#{id}, #{uid}, #{content}, #{type}, #{camouflage}, #{tag}, #{permissions}, #{status}, #{address}, #{showAddress}, #{time}",
            ")"
    })
    Integer addDynamic(Dynamic dynamic);
    
    // 移除动态
    @Delete("delete from scp_dynamic where id = #{id}")
    Integer removeDynamic(Integer id);
    
    // 修改动态查看权限
    @Update({
            "update scp_dynamic",
            "set permissions = #{permissions}",
            "where id = #{id}"
    })
    Integer updateDynamicPermissions(Integer id, Integer permissions);
    
    // 根据did获取用户id
    @Select("select uid from scp_dynamic where id = #{did}")
    Integer getUidByDid(Integer did);
    
    // 获取最大的id
    @Select("select max(id) from scp_dynamic")
    Integer getMaxId();
    
    // 根据id获取动态
    @Select("select * from scp_dynamic where id = #{id}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    Dynamic findById(Integer id);
    
    // 通过关键字模糊搜索动态内容
    @Select("select * from scp_dynamic where content like concat('%', #{keyword}, '%') and permissions = 1 order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> searchByKeyword(String keyword);
    
    // 根据学校id获取动态列表
    @Select("select * from scp_dynamic where uid in (select id from scp_user where school = #{school}) and permissions = 1 order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listDynamicBySchool(Integer school);
    
    // 地址查询动态列表
    @Select("select * from scp_dynamic where address like concat('%', #{address}, '%') and show_address = 1 and permissions = 1 order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listDynamicByAddress(String address);
    
    // 根据用户uid返回动态列表
    @Select("select * from scp_dynamic where uid = #{uid} order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listDynamicByUid(Integer uid);
    
    // 返回用户uid的好友动态列表
    @Select({
            "select * from scp_dynamic",
            "where",
            "uid in (select a.fid from scp_friend a, scp_friend b where a.fid = b.uid and b.fid = a.uid and a.uid = #{uid})",
            "and permissions != 0",
            "order by time desc"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listFriendDynamicByUid(Integer uid);
    
    // 返回用户uid的关注动态列表
    @Select("select * from scp_dynamic where uid in (select fid from scp_friend where uid = 1) and permissions = 1 order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listAttentionDynamicByUid(Integer uid);
    
    @Select("select * from scp_dynamic where uid = #{uid} and permissions = #{permissions} order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listDynamicByUidAndPermissions(Integer uid, Integer permissions);
    
    // 返回动态列表
    @Select("select * from scp_dynamic order by time desc")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "show_address", property = "showAddress", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "uid", jdbcType = JdbcType.INTEGER),
            @Result(column = "uid", property = "user", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
            @Result(column = "id", property = "resourceList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicResourceMapper.listByDid")),
            @Result(column = "id", property = "discussList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicDiscussMapper.listByDid")),
            @Result(column = "id", property = "likesList", javaType = List.class,
                    many = @Many(select = "com.codergoo.mapper.DynamicLikesMapper.listByDid"))
    })
    List<Dynamic> listDynamic();
    
    // 根据动态id和用户uid获取动态条数
    @Select("select count(*) from scp_dynamic where id = #{id} and uid = #{uid}")
    Integer hasDynamic(Integer uid, Integer id);
}
