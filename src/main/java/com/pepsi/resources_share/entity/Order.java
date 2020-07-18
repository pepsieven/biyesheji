package com.pepsi.resources_share.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author pepsiL
 * @create 2020-03-01 11:28 上午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_order")
public class Order {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer consumerId;

    private String resourceId;

    private BigDecimal price;

    private Integer authorId;

    private String consumerName;

    private String authorName;

    private String orderNumber;

    private Date createTime;

    private Boolean disabled;


}
