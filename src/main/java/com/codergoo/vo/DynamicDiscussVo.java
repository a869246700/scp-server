package com.codergoo.vo;

import com.codergoo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 动态评论Vo
 *
 * @author coderGoo
 * @date 2021/2/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicDiscussVo {
    
    private Integer did; // 动态id
    private String content; // 评论内容
    private Integer rid; // 被评论人id
    private Date time; // 评论时间
}
