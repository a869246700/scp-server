package com.codergoo.service;

import java.util.Map;

/**
 * 短信业务服务
 * @author coderGoo
 * @date 2021/2/24
 */
public interface SmsService {
    
    // 发送短信
    boolean sendSms(String phoneNumber, String templateCode, Map<String, Object> code);
}
