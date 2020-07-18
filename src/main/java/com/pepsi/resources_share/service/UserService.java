package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
public interface UserService extends IService<User> {


    UserDetails loadUserByUsername(String username);

    User getUserId(String username);

    int updateUser(User user);

    int register(User user);

    List<User> getApplyAuthor();

    Object confirmApply(int id);

    Object rejectApply(int id);


}
