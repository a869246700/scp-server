package com.codergoo.mapper;

import com.codergoo.domain.Account;
import com.codergoo.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账号持久层
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Repository
@Mapper
public interface AccountMapper {
    
    // 获取所有账号
    @Select("select * from scp_account")
    List<Account> findAll();
    
    // 根据 username 查询账号
    @Select("select * from scp_account where username = #{username}")
    Account findByUsername(String username);
    
    // 根据 id 查询账号
    @Select("select * from scp_account where id = #{id}")
    Account findById(Integer id);
    
    // 获取最大的id
    @Select("select max(id) from scp_account")
    Integer getMaxId();
    
    // 创建账号
    @Insert({
            "insert into",
            "scp_account(",
            "id, username, password, status, type",
            ")",
            "values(",
            "#{id}, #{username}, #{password}, #{status}, #{type}",
            ")"
    })
    Integer addAccount(Account account);
    
    // 删除账户
    @Delete("delete from scp_account where id = #{id}")
    Integer removeAccount(Integer id);
    
    // 修改账号类型
    @Update({
            "update scp_account",
            "set type = #{type}",
            "where id = #{id}"
    })
    Integer updateAccountType(Integer id, Integer type);
    
    // 修改账号状态
    @Update({
            "update scp_account",
            "set status = #{status}",
            "where id = #{id}"
    })
    Integer updateAccountStatus(Integer id, Integer status);
    
    // 修改账号密码
    @Update({
            "update scp_account",
            "set password = #{password}",
            "where id = #{id}"
    })
    Integer updateAccountPassword(Integer id, String password);
}
