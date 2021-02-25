package com.codergoo.service;

import com.codergoo.domain.Account;
import com.codergoo.domain.User;

/**
 * token的业务逻辑层
 * @author coderGoo
 * @date 2021/2/23
 */
public interface TokenService {
    
    // 获取token, 并且保存到redis
    String generateToken(Account account);
    
    // 根据token获取账号信息account
    Account getAccountByToken(String token);
    
    // 根据token获取用户信息user
    User getUserByToken(String token);
    
    // 移除token
    Boolean removeToken(Integer accountId);
    
    // 验证token
    void authToken(String token);
    
    // 获取token内的id
    Integer getIdByToken(String token);
}
