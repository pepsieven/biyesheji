package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.UserIncome;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
public interface UserIncomeService extends IService<UserIncome> {

    int removeByUserid(int userid);

    IPage<UserIncome> pageByUserid(Page page, int userid);

}
