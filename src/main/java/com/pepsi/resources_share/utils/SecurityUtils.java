package com.pepsi.resources_share.utils;

import com.pepsi.resources_share.entity.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author pepsiL
 * @create 2020-03-08 5:26 下午
 */
public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static CurrentUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CurrentUser)) {
            // 如果获取到的不是我们创建的实体类那就说明没登陆，获取到的是String，待会可以Debug给你看
            return null;
        }

        return (CurrentUser) principal;
    }
}
