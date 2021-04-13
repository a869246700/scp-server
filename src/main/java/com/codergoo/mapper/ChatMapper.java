package com.codergoo.mapper;

import com.codergoo.domain.ChatMsg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天记录持久层
 *
 * @author coderGoo
 * @date 2021/4/13
 */
@Repository
@Mapper
public interface ChatMapper {
    
    @Select("select * from scp_chat where (uid = #{u1} and fid = #{u2}) or (uid = #{u2} and fid = #{u1})")
    List<ChatMsg> getChatRecord(Integer u1, Integer u2);
    
    @Delete("delete from scp_chat where id = #{id}")
    Integer delChatRecordRow(Integer id);
}
