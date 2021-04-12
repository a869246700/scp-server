package com.codergoo.mapper;

import com.codergoo.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息 持久化层
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Repository
@Mapper
public interface UserMapper {

    // 根据id获取 user
    @Select("select * from scp_user where id = #{id}")
    User findById(Integer id);
    
    // 根据用户名模糊查询
    @Select("select * from scp_user where nickname like concat('%', #{nickname}, '%')")
    List<User> listUserByNickname(String nickname);
    
    // 添加用户信息
    @Insert({
            "insert into",
            "scp_user(",
            "id, nickname, school, address, email, avatar, gender, signature, birthday, fullname",
            ")",
            "values(",
            "#{id}, #{nickname}, #{school}, #{address}, #{email}, #{avatar}, #{gender}, #{signature}, #{birthday}, #{fullname}",
            ")"
    })
    Integer addUser(User user);
    
    // 修改用户信息
    @Update({
            "update scp_user",
            "set nickname = #{nickname}, school = #{school}, address = #{address},",
            "email = #{email}, avatar = #{avatar}, gender = #{gender},",
            "signature = #{signature}, birthday = #{birthday}, fullname = #{fullname}",
            "where id = #{id}"
    })
    Integer updateUser(User user);
    
    // 修改用户头像
    @Update({
            "update scp_user",
            "set avatar = #{avatar}",
            "where id = #{id}"
    })
    Integer updateUserAvatar(Integer id, String avatar);
}
