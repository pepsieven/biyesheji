package com.pepsi.resources_share.emuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pepsiL
 * @create 2020-03-09 11:47 上午
 */
@AllArgsConstructor
@Getter
public enum Role {

    /**
     * 创作者
     */
    CREATOR("2"),

    /**
     * 非创作者
     */
    NON_CREATOR("1");

    private final String value;
}
