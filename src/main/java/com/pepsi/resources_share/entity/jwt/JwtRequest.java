package com.pepsi.resources_share.entity.jwt;

import lombok.Data;

import java.io.Serializable;

/**
 * <b><code>JwtRequest</code></b>
 * <p/>
 * 登录请求类
 * <p/>
 * <b>Creation Time:</b> 2020/2/10 17:04.
 *
 * @author Li Shangzhe
 * @since czps2s4ada-shared-api 0.1.0
 */
@Data
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;
}