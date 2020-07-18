package com.pepsi.resources_share.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author pepsiL
 * @create 2020-03-01 12:01 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderAlipay {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private String alipayId;

    private BigDecimal alipayPrice;

    private Boolean disabled;

    private Date createTime;

}
