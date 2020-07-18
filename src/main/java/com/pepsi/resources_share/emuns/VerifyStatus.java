package com.pepsi.resources_share.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pepsiL
 * @create 2020-03-24 11:35 上午
 */
@AllArgsConstructor
@Getter
public enum  VerifyStatus {
    /**
     * 认证后的申请状态
     */
    SUCCESS_APPLY("2"),

    REJECT_APPLY("0"),

    /**
     * 申请成为作者
     */
    APPLY_AUTHOR("1");

    private final String value;
}
