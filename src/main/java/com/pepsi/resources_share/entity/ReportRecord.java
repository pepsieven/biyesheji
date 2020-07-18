package com.pepsi.resources_share.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author pepsiL
 * @create 2020-05-06 3:25 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReportRecord {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String response;

    private Integer uid;

    private String reportReason;

    private Boolean disabled;

}
