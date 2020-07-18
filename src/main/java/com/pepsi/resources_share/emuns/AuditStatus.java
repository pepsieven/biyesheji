package com.pepsi.resources_share.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pepsiL
 * @create 2020-03-19 4:36 下午
 */
@AllArgsConstructor
@Getter
public enum  AuditStatus {

    /**
     * 通过审核的资源
     */
    PASS_RESOURCE("3"),

    /**
     * 未通过审核的资源
     */
    NOT_PASS_RESOURCE("2"),

    /**
     * 取消上传
     */
    CANCEL_UPLOAD("0"),

    /**
     * 审核中的资源
     */
    AUdit_REOURCE("1");



    private final String value;
}
