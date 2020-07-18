package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.emuns.Role;
import com.pepsi.resources_share.emuns.VerifyStatus;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.User;
import com.pepsi.resources_share.mapper.UserMapper;
import com.pepsi.resources_share.service.UserService;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {


    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("User not exist");
        }

        return new CurrentUser(user.getId(), username, user.getPassword(), user.getRole());
    }

    @Override
    public User getUserId(String username) {
        return baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public int updateUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 对明文密码加密
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);
        return baseMapper.updateById(user);
    }

    @Override
    public int register(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 对明文密码加密
        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodedPassword);
        return baseMapper.insert(user);

    }

    @Override
    public List<User> getApplyAuthor() {
        return baseMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getVerifyStatus, VerifyStatus.APPLY_AUTHOR.getValue()));
    }

    @Override
    public Object confirmApply(int id) {
        User user = new User();
        user.setId(id);
        user.setVerifyStatus(VerifyStatus.SUCCESS_APPLY.getValue());
        user.setRole(Role.CREATOR.getValue());
        return baseMapper.updateById(user);
    }

    @Override
    public Object rejectApply(int id) {
        User user = new User();
        user.setId(id);
        user.setVerifyStatus(VerifyStatus.REJECT_APPLY.getValue());
        return baseMapper.updateById(user);
    }

}
