package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author coderGoo
 * @date 2021/4/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {
    
    private Integer id; // 记录id
    private Integer uid; // 发送人id
    private Integer fid; // 接收人id
    private String msg; // 消息内容
    private Integer type; // 类型
    private Date time; // 发送时间
}
