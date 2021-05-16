package com.codergoo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友实体
 *
 * @author coderGoo
 * @date 2021/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend implements Serializable {

    private Integer fid; // 好友id
    private String remark; // 备注
    private Integer uid; // 用户id
    private Integer status; // 状态
    private Integer gid; // 群组id
    private Date time; // 添加时间
    
    private User friend; // 好友信息
}
