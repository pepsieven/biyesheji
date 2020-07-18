package com.pepsi.resources_share.entity.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <b><code>JwtResponse</code></b>
 * <p/>
 * 登录响应类
 * <p/>
 * <b>Creation Time:</b> 2020/2/10 17:04.
 *
 * @author Li Shangzhe
 * @since czps2s4ada-shared-api 0.1.0
 */
@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private String token;
}