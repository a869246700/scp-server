package com.codergoo.controller;

import com.alibaba.fastjson.JSONObject;
import com.codergoo.annotation.AccountLoginToken;
import com.codergoo.common.entity.Result;
import com.codergoo.common.utils.ResultUtil;
import com.codergoo.domain.Account;
import com.codergoo.domain.User;
import com.codergoo.service.AccountService;
import com.codergoo.service.MailService;
import com.codergoo.service.TokenService;
import com.codergoo.service.UserService;
import com.codergoo.utils.RedisUtil;
import com.codergoo.vo.MailVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 账户模块接口
 *
 * @author coderGoo
 * @date 2021/2/23
 */

@RestController
@RequestMapping("/api/account")
@CrossOrigin // 开启跨域支持
public class AccountController {
    
    @Autowired
    public TokenService tokenService;
    
    @Autowired
    public AccountService accountService;
    
    @Autowired
    public UserService userService;
    
    @Autowired
    public MailService mailService;
    
    @Autowired
    public RedisUtil redisUtil;
    
    @Value("${vc.mail.account.login.tag}")
    private String loginVcTag;
    
    @Value("${vc.mail.account.register.tag}")
    private String registerVcTag;
    
    @Value("${vc.expire-time}")
    private Integer vcExpireTime;
    
    // 获取注册邮件验证码
    @GetMapping("/getRegisterVc")
    public Result getRegisterVerificationCode(String to) {
        // 1. 判断用户是否已经存在
        Boolean isExist = accountService.isExist(to);
        if (isExist) {
            return ResultUtil.error(200, "邮箱：" + to + " 已被注册，请前往登录！");
        }
        // 2. 获取随机验证码
        String registerVc = RandomStringUtils.randomNumeric(6);
    
        // 3. 设置邮件对象
        MailVo mailVo = new MailVo();
        mailVo.setTo(to);
        mailVo.setSubject("【SCP】：账号注册验证码");
        mailVo.setText("账号注册验证码有效时间为：" + vcExpireTime + "分钟！验证码：" + registerVc);
        MailVo sendMail = mailService.sendMail(mailVo);
    
        if ("ok".equals(sendMail.getStatus())) {
            // 3. 将登录验证码保存到 redis 中
            redisUtil.setEx(registerVcTag + to, registerVc, vcExpireTime, TimeUnit.MINUTES);
            return ResultUtil.success(200, "验证码发送成功!");
        }
        return ResultUtil.success(200, "验证码发送失败!");
    }
    
    // 获取登录邮件验证码
    @GetMapping("/getLoginVc")
    public Result getLoginVerificationCode(String to) {
        // 1. 判断用户是否存在
        // 1. 判断用户是否已经存在
        Boolean isExist = accountService.isExist(to);
        if (!isExist) {
            return ResultUtil.error(200, "账号：" + to + " 未注册，请前往注册！");
        }
        // 用户存在之后的操作
        // 2. 取随机验证码
        String loginVc = RandomStringUtils.randomNumeric(6);

        // 3. 设置邮件对象
        MailVo mailVo = new MailVo();
        mailVo.setTo(to);
        mailVo.setSubject("【SCP】：账号登录验证码");
        mailVo.setText("账号登录验证码有效时间为：" + vcExpireTime + "分钟！验证码：" + loginVc);
        MailVo sendMail = mailService.sendMail(mailVo);
        
        if ("ok".equals(sendMail.getStatus())) {
            // 3. 将登录验证码保存到 redis 中
            redisUtil.setEx(loginVcTag + to, loginVc, vcExpireTime, TimeUnit.MINUTES);
            return ResultUtil.success(200, "验证码发送成功!");
        }
        return ResultUtil.success(200, "验证码发送失败!");
    }
    
    // 注册：正常用户名密码
    @PostMapping("/register")
    public Result register(Account account) {
        // 1. 保证健壮性，判断是否存在 username 和 password
        if (StringUtils.isBlank(account.getUsername())) {
            return ResultUtil.error(200, "请确保账号输入不为空！");
        }
        if (StringUtils.isBlank(account.getPassword())) {
            return ResultUtil.error(200, "请确保密码输入不为空！");
        }
        
        // 2. 判断用户是否已经存在
        Boolean isExist = accountService.isExist(account.getUsername());
        // 已存在
        if (isExist) {
            return ResultUtil.error(200, "已存在该账号!");
        }
        
        // 3. 获取最大id
        Integer id = accountService.getMaxId();
        account.setId(id);
        
        // 4. 设置默认值
       account.setType(1); // 账号类型：账号密码
        
        // 5. 添加账号
        Boolean addAccountSuccess = accountService.addAccount(account);
        if (!addAccountSuccess) {
            return ResultUtil.error(500, "注册账号失败！");
        }
        
        // 6. 设置token
        String token = tokenService.generateToken(account);
        
        // 7. 生成默认的用户信息
        User user = new User();
        user.setId(id);
        userService.addUser(user);
        
        // 8. 获取返回数据
        JSONObject accountObject = new JSONObject();
        accountObject.put("id", id);
        accountObject.put("username", account.getUsername());
    
        JSONObject resultObject = new JSONObject();
        resultObject.put("account", accountObject);
        resultObject.put("user", user);
        resultObject.put("token", token);
        
        return ResultUtil.success(resultObject);
    }
    
    // 登录：正常用户名密码
    @PostMapping("/login")
    public Result login(Account account) {
        // 1. 保证健壮性，判断是否存在 username 和 password
        if (StringUtils.isBlank(account.getUsername())) {
            return ResultUtil.error(200, "请确保用户名输入不为空！");
        }
        if (StringUtils.isBlank(account.getPassword())) {
            return ResultUtil.error(200, "请确保密码输入不为空！");
        }
    
        // 2. 判断用户是否存在
        Account accountForBase = accountService.findAccountByUsername(account.getUsername());
        if (null == accountForBase) {
            return ResultUtil.error(200, "登录失败,用户不存在!");
        }
        // 3. 校验密码
        if (!accountForBase.getPassword().equals(account.getPassword())) {
            return ResultUtil.error(200, "登录失败,密码错误!");
        }
        // 4. 设置token
        String token = tokenService.generateToken(accountForBase);
        // 5. 返回登录成功的数据
        JSONObject resultObject = new JSONObject();
        
        // 账号包含 id 和 username
        JSONObject accountObject = new JSONObject();
        accountObject.put("id", accountForBase.getId());
        accountObject.put("username", accountForBase.getUsername());
        
        // 用户信息包含：所有信息
        User user = userService.findUserById(accountForBase.getId());
        resultObject.put("account", accountObject);
        resultObject.put("user", user);
        resultObject.put("token", token);
        
        return ResultUtil.success(resultObject);
    }
    
    // 注册：邮件验证码
    @PostMapping("/registerByMailVc")
    public Result registerByMailVc(String username, String registerVc) {
        // 1. 保证健壮性，判断是否存在 username 和 loginVc
        if (StringUtils.isBlank(username)) {
            return ResultUtil.error(200, "请确保用户名输入不为空！");
        }
        if (StringUtils.isBlank(registerVc)) {
            return ResultUtil.error(200, "请确保验证码输入不为空！");
        }
        
        // 2. 判断用户是否已经存在
        Boolean isExist = accountService.isExist(username);
        if (isExist) {
            return ResultUtil.error(200, "获取验证码失败，已存在该账号!");
        }
        
        // 3. 校验验证码是否有效
        // 3.1 获取验证码
        String loginVcKey = registerVcTag + username; // 保存在 redis 中的 key
        Object registerVcRedisObject = redisUtil.get(loginVcKey);
        // 3.2 判断验证码是否过期
        if (null == registerVcRedisObject) {
            return ResultUtil.error(200, "账号注册验证码过期，请重新获取！");
        }
        String registerVcRedis = String.valueOf(registerVcRedisObject);
        // 3.3 进行验证码校验
        if (!registerVc.equals(registerVcRedis)) {
            return ResultUtil.error(200, "账号注册验证码错误，请重新输入！");
        }

        // 4. 通过校验后，后续生成Account
        Account account = new Account();
        // 5. 获取最大id
        Integer id = accountService.getMaxId();
        account.setId(id);
        account.setUsername(username);
        account.setPassword("123456");
    
        // 6. 设置默认值
        account.setType(2); // 邮箱账号
    
        // 7. 添加账号
        Boolean addAccountSuccess = accountService.addAccount(account);
        if (!addAccountSuccess) {
            return ResultUtil.error(500, "注册账号失败！");
        }
    
        // 8. 设置token
        String token = tokenService.generateToken(account);
    
        // 9. 生成默认的用户信息
        User user = new User();
        user.setId(id);
        user.setEmail(username);
        userService.addUser(user);
    
        // 10. 获取返回数据
        JSONObject accountObject = new JSONObject();
        accountObject.put("id", id);
        accountObject.put("username", account.getUsername());
    
        JSONObject resultObject = new JSONObject();
        resultObject.put("account", accountObject);
        resultObject.put("user", user);
        resultObject.put("token", token);
        
        // 11. 删除redis中的验证码
        redisUtil.delete(loginVcKey);
        return ResultUtil.success(resultObject);
    }
    
    // 登录：邮件验证码
    @PostMapping("/loginByMailVc")
    public Result loginByMailVc(String username, String loginVc) {
        // 1. 保证健壮性，判断是否存在 username 和 loginVc
        if (StringUtils.isBlank(username)) {
            return ResultUtil.error(200, "请确保用户名输入不为空！");
        }
        if (StringUtils.isBlank(loginVc)) {
            return ResultUtil.error(200, "请确保验证码输入不为空！");
        }
        // 2. 判断用户是否存在
        Account accountForBase = accountService.findAccountByUsername(username);
        if (null == accountForBase) {
            return ResultUtil.error(200, "获取验证码失败,用户不存在!");
        }
        // 3. 校验验证码是否有效
        // 3.1 获取验证码
        String loginVcKey = loginVcTag + username; // 保存在 redis 中的 key
        Object loginVcRedisObject = redisUtil.get(loginVcKey);
        // 3.2 判断验证码是否过期
        if (null == loginVcRedisObject) {
            return ResultUtil.error(200, "账号登录验证码过期，请重新获取！");
        }
        String loginVcRedis = String.valueOf(loginVcRedisObject);
        // 3.3 进行验证码校验
        if (!loginVc.equals(loginVcRedis)) {
            System.out.println("loginVcRedis: " + loginVcRedis);
            return ResultUtil.error(200, "账号登录验证码错误，请重新输入！");
        }
        // 4. 通过校验后，设置token
        String token = tokenService.generateToken(accountForBase);
        // 5. 返回登录成功的数据
        JSONObject resultObject = new JSONObject();
    
        // 账号包含 id 和 username
        JSONObject accountObject = new JSONObject();
        accountObject.put("id", accountForBase.getId());
        accountObject.put("username", accountForBase.getUsername());
    
        // 用户信息包含：所有信息
        User user = userService.findUserById(accountForBase.getId());
        resultObject.put("account", accountObject);
        resultObject.put("user", user);
        resultObject.put("token", token);
    
        // 6. 删除redis中的登录验证码
        redisUtil.delete(loginVcKey);
        return ResultUtil.success(resultObject);
    }
    
    // 退出登录
    @GetMapping("/logout")
    @AccountLoginToken // 需要通过token校验
    public Result logout(HttpServletRequest httpServletRequest) {
        // 1. 根据 token 获取用户信息
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        Account account = tokenService.getAccountByToken(token);
        // 2. 移除 redis 中用户的token
        Boolean logoutStatus = tokenService.removeToken(account.getId());
        if (logoutStatus) {
            return ResultUtil.success(200, "退出成功！");
        }
        return ResultUtil.error(200, "退出失败！");
    }
    
    // 修改密码
    @PostMapping("/updatePassword")
    @AccountLoginToken // 需要通过token校验
    public Result updatePassword(Integer id, String password) {
        Boolean updateSuccess = accountService.updateAccountPassword(id, password);
        if (updateSuccess) {
            return ResultUtil.success(200, "密码修改成功！");
        }
        return ResultUtil.error(500, "密码修改失败！");
    }
}
