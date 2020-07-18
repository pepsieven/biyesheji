package com.pepsi.resources_share.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author pepsiL
 * @create 2020-04-01 2:17 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CheckWord {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String resourceId;

    private String illegalWord;

    private Integer illegalNum;

    private String disabled;
}
