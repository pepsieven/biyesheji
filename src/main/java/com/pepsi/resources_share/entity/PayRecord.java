package com.pepsi.resources_share.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author pepsiL
 * @create 2020-03-18 7:34 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayRecord {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer userId;

    private String alipayNumber;

    private Boolean disabled;
}
