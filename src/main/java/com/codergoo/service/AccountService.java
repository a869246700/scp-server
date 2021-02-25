package com.codergoo.service;

import com.codergoo.domain.Account;

/**
 * 账户业务层接口
 *
 * @author coderGoo
 * @date 2021/2/23
 */
public interface AccountService {

    // 根据 username 查询 account
    Account findAccountByUsername(String username);
    
    // 根据 id 查询 account
    Account findAccountById(Integer id);
    
    // 获取最大的id
    Integer getMaxId();
    
    // 判断用户是否存在
    Boolean isExist(String username);
    
    // 添加账号
    Boolean addAccount(Account account);
    
    // 修改账号类型
    Boolean updateAccountType(Integer id, Integer type);
    
    // 修改账号状态
    Boolean updateAccountStatus(Integer id, Integer status);
    
    // 修改账号密码
    Boolean updateAccountPassword(Integer id, String password);
}
