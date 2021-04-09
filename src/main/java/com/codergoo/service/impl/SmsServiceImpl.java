package com.codergoo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.codergoo.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coderGoo
 * @date 2021/2/24
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    
    @Value("${sms.aliyun.accessKeyId}")
    private String accessKeyId;
    
    @Value("${sms.aliyun.accessSecret}")
    private String accessSecret;
    
    @Value("${sms.aliyun.signName}")
    private String signName;
    
    @Override
    public boolean sendSms(String phoneNumber, String templateCode, Map<String, Object> code) {
        log.info("accessKeyId：" + accessKeyId);
        log.info("accessSecret：" + accessSecret);
        log.info("signName：" + signName);
        // 1. 连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
    
        // 2. 构建请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com"); // 不要动
        request.setSysVersion("2017-05-25"); // 不要动
        request.setSysAction("SendSms");
    
        // 3. 自定义参数（手机号，验证码，签名，模板）
        // request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber); // 添加手机号
        request.putQueryParameter("SignName", signName); // 添加签名名
        request.putQueryParameter("TemplateCode", templateCode); // 添加模板编码
    
        // 4. 构建短信验证码
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code); // 验证码
        request.putBodyParameter("TemplateCodeParam", JSONObject.toJSONString(map)); // 添加短信验证码
    
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
