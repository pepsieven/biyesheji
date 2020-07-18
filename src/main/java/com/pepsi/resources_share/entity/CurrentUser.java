package com.pepsi.resources_share.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * <p>
 * 直接继承Spring Security的用户类扩展即可
 * 因为Spring Security的用户已经有username password所以不用设置
 * 建议这种有自定义属性的，最好重新建一个实体类区分
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
public class CurrentUser extends User {

    @Getter
    @Setter
    private Integer id;

    public CurrentUser(int id, String username, String password, String role) {
        super(username, password, AuthorityUtils.createAuthorityList(role));
        this.setId(id);
    }
}
