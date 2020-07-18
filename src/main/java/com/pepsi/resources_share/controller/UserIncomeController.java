package com.pepsi.resources_share.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.UserIncome;
import com.pepsi.resources_share.service.UserIncomeService;
import com.pepsi.resources_share.service.UserService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/user-income")
public class UserIncomeController {
    @Resource
    UserIncomeService userIncomeService;
    @Resource
    UserService userService;


    /**
     * 管理员和用户自身查看收入记录皆可调用
     * 根据所选用户进行分页查询其收入记录
     *
     * @param page
     * @return
     */
    @RequestMapping("/pageListByUid")
    public Object pageListByUid(@RequestParam(defaultValue = "1") int page) {
        //获得当前登陆的用户信息
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        Page<UserIncome> p = new Page<>(page, 6);
        return userIncomeService.pageByUserid(p, currentUser.getId());

    }

    /**
     * 修改单条收入记录
     * 对应要考虑用户余额表的修改
     *
     * @param userIncome
     * @return
     */
    @RequestMapping("/updateOneUIC")
    public Object updateOneUIC(UserIncome userIncome) {
        return userIncomeService.updateById(userIncome);
    }


}
