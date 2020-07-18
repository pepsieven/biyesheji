package com.pepsi.resources_share.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author pepsiL
 * @create 2020-03-18 7:40 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_attention_author")
public class UserAttention {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;

    private Integer authorId;

    private String userName;

    private String authorName;

    private Boolean disabled;
}
