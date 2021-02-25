package com.codergoo.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codergoo.domain.Account;
import com.codergoo.domain.User;
import com.codergoo.service.AccountService;
import com.codergoo.service.TokenService;
import com.codergoo.service.UserService;
import com.codergoo.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * token 业务层实现类
 * @author coderGoo
 * @date 2021/2/23
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    
    @Value("${token.expire-time}")
    public String tokenExpireTime;
    
    @Autowired
    public RedisUtil redisUtil;
    
    @Autowired
    public AccountService accountService;
    
    @Autowired
    public UserService userService;
    
    @Value("${token.flag}")
    public String accountTokenStr;
    
    @Override
    public String generateToken(Account account) {
        // 登录成功后：生成JWT，保存到redis
        
        // 1. 判断是否已经存在token了，以防重复获取。等到过期了再去获取，否则直接获取 redis 中的 token即可
        Object tokenObj = redisUtil.get(accountTokenStr + account.getId());
        String token;
        // 2. 如果不为空，则直接返回token
        if (tokenObj != null) {
            token = String.valueOf(tokenObj);
            return token;
        }
        
        // 3. 获取服务器token是否过期时间
        Integer tokenExpireTimeVal = Integer.valueOf(tokenExpireTime) * 60 * 60; // 进行转换 8 * 60 * 60
        Map<String, Object> map = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, tokenExpireTimeVal);
    
        // 4. 生成最新token
        // JWT的header部分,该map可以是空的,因为有默认值{"alg":HS256,"typ":"JWT"}
        token = JWT.create()
                .withHeader(map)
                .withClaim("aid",account.getId()) //添加payload
                .withClaim("username",account.getUsername())
                .withExpiresAt(instance.getTime()) //设置过期时间
                .withAudience(String.valueOf(account.getId())) // 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(account.getPassword())); // 以 scp 作为 token 的密钥
        
        // 4. 将token保存到 redis 中
        redisUtil.set(accountTokenStr + account.getId(), token, tokenExpireTimeVal);
        return token;
    }
    
    @Override
    public Account getAccountByToken(String token) {
        Integer id = this.getIdByToken(token);
        // 返回用户信息
        return accountService.findAccountById(id);
    }
    
    @Override
    public User getUserByToken(String token) {
        Integer id = this.getIdByToken(token);
        return userService.findUserById(id);
    }
    
    @Override
    public Integer getIdByToken(String token) {
        // 执行认证
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        return Integer.valueOf(userId);
    }
    
    @Override
    public Boolean removeToken(Integer accountId) {
        try {
            redisUtil.del(accountTokenStr + accountId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public void authToken(String token) {
        // 获取账号信息
        Account account = this.getAccountByToken(token);
        if (account == null) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
        try {
            // 1. 获取JWT
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Integer aid = decodedJWT.getClaim("aid").asInt(); // 获取账户id
            if (aid == null) {
                throw new RuntimeException("token已过期，请重新获取！");
            }
            // 2. 获取 redis 中，账户对应的token
            String redisToken = String.valueOf(redisUtil.get(accountTokenStr + aid));
            if (!token.equals(redisToken)) {
                throw new RuntimeException("token错误，请重新获取！");
            }
            // log.info("userId: " + decodedJWT.getClaim("aid").asInt());
            // log.info("username: " + decodedJWT.getClaim("username").asString());
            // log.info("过期时间: " + decodedJWT.getExpiresAt());
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }
    }
}
