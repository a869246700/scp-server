package com.codergoo.mapper;

import com.codergoo.domain.Message;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author coderGoo
 * @date 2021/4/13
 */
@Repository
@Mapper
public interface MessageMapper {
    
    @Select("select * from scp_message where uid = #{uid}")
    @Results({
            @Result(column = "from", property = "from", jdbcType = JdbcType.INTEGER),
            @Result(column = "from", property = "fromUser", javaType = User.class,
                    one = @One(select = "com.codergoo.mapper.UserMapper.findById")),
    })
    List<Message> getMessageList(Integer uid);
    
    @Delete("delete from scp_message where id = #{id}")
    Integer delMessageById(Integer id);
    
    @Insert({
            "insert into scp_message(",
            "uid, content, from, did, type, time",
            ") values(",
            "#{uid}, #{content}, #{from}, #{did}, #{type}, #{time}",
            ")"
    })
    Integer insertMessage(Message message);
}
