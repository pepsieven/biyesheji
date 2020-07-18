package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.UserIncome;
import com.pepsi.resources_share.mapper.UserIncomeMapper;
import com.pepsi.resources_share.service.UserIncomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@Service
public class UserIncomeServiceImpl extends ServiceImpl<UserIncomeMapper, UserIncome> implements UserIncomeService {
    @Resource
    UserIncomeMapper userIncomeMapper;

    @Override
    public int removeByUserid(int userid) {
        return baseMapper.update(new UserIncome(), Wrappers.<UserIncome>lambdaUpdate().eq(UserIncome::getUserId, userid).set(UserIncome::getDisabled, 1));
    }

    @Override
    public IPage<UserIncome> pageByUserid(Page page, int userid) {
        return baseMapper.selectPage(page, Wrappers.<UserIncome>lambdaQuery().eq(UserIncome::getUserId, userid));
    }
}
