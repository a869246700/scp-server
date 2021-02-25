package com.codergoo.service.impl;

import com.codergoo.domain.Account;
import com.codergoo.mapper.AccountMapper;
import com.codergoo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户业务 逻辑实现类
 *
 * @author coderGoo
 * @date 2021/2/23
 */
@Service
public class AccountServiceImpl implements AccountService {
    
    @Autowired
    public AccountMapper accountMapper;
    
    @Override
    public Account findAccountByUsername(String username) {
        return accountMapper.findByUsername(username);
    }
    
    @Override
    public Account findAccountById(Integer id) {
        return accountMapper.findById(id);
    }
    
    @Override
    public Integer getMaxId() {
        Integer id = accountMapper.getMaxId();
        return  id == null ? 1 : id + 1;
    }
    
    @Override
    public Boolean isExist(String username) {
        Account account = accountMapper.findByUsername(username);
        return account != null;
    }
    
    @Override
    public Boolean addAccount(Account account) {
        // 设置账号状态 0：未激活， 1：激活， 2：封号
        account.setStatus(1);
        Integer integer = accountMapper.addAccount(account);
        return integer == 0;
    }
    
    @Override
    public Boolean updateAccountType(Integer id, Integer type) {
        Integer integer = accountMapper.updateAccountType(id, type);
        return integer == 1;
    }
    
    @Override
    public Boolean updateAccountStatus(Integer id, Integer status) {
        Integer integer = accountMapper.updateAccountStatus(id, status);
        return integer == 1;
    }
    
    @Override
    public Boolean updateAccountPassword(Integer id, String password) {
        Integer integer = accountMapper.updateAccountPassword(id, password);
        return integer == 1;
    }
}
