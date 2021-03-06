package com.pepsi.resources_share.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author pepsiL
 * @create 2020-05-07 8:43 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ResourceScore {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String response;

    private Integer uid;

    private Integer score;

    private Boolean disabled;



}
