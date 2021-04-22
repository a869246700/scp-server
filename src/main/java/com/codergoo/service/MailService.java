package com.codergoo.service;

import com.codergoo.vo.MailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 邮件业务逻辑
 *
 * @author coderGoo
 * @date 2021/2/23
 */
public interface MailService {
    
    MailVo sendMail(MailVo mailVo);
    
    // 检测邮件信息类
    void checkMail(MailVo mailVo);
    
    // 构建复杂的邮件信息
    void sendMimeMail(MailVo mailVo);
    
    // 保存邮件
    MailVo saveMail(MailVo mailVo);
    
    // 获取邮件发信人
    String getMailSendFrom();
}
