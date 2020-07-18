package com.pepsi.resources_share.controller;


import com.pepsi.resources_share.entity.*;
import com.pepsi.resources_share.service.*;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/user-balance")
public class UserBalanceController {

    private final UserService userService;

    private final UserBalanceService userBalanceService;

    private final OrderService orderService;

    private final UserIncomeService userIncomeService;

    private final UserOutlayService userOutlayService;

    public UserBalanceController(UserService userService, UserBalanceService userBalanceService, OrderService orderService, UserIncomeService userIncomeService, UserOutlayService userOutlayService) {
        this.userService = userService;
        this.userBalanceService = userBalanceService;
        this.orderService = orderService;
        this.userIncomeService = userIncomeService;
        this.userOutlayService = userOutlayService;
    }

    /**
     * 查看所有用户的余额情况
     *
     * @return
     */
    @RequestMapping("/listAllUb")
    public Object listAllUb() {
        return userBalanceService.list();
    }

    /**
     * 修改用户余额表（根据页面要对余额表修改的属性进行修改）
     *
     * @return
     */
    @RequestMapping("/changeBalance")
    public Object changeBalance(UserBalance userBalance) {
        return userBalanceService.updateById(userBalance);
    }

    /**
     * 根据订单资源消费记录进行余额的更新
     *
     * @return
     */
    @PostMapping("/cBBO")
    @Transactional(propagation = Propagation.REQUIRED)
    public Object changeBalanceByOrder(Order order) {
        //1、取得此次交易订单的金额
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        Order byCnAndRi = orderService.getByCnAndRi(currentUser.getUsername(), order.getResourceId());

        BigDecimal price = byCnAndRi.getPrice();

        BigDecimal userBalance = userBalanceService.getUserBalance(currentUser.getId());

        //2、更新消费者和作者收入表和支出表
        //   消费者
        if (userBalance.subtract(price).compareTo(BigDecimal.ZERO) != -1) {
            UserOutlay consumerOutlay = new UserOutlay();
            consumerOutlay.setOutlay(price);
            consumerOutlay.setUserId(currentUser.getId());
            consumerOutlay.setOrderId(byCnAndRi.getId());
            //   作者
            UserIncome authorIncome = new UserIncome();
            authorIncome.setIncome(price);
            authorIncome.setUserId(byCnAndRi.getAuthorId());
            authorIncome.setOrderId(byCnAndRi.getId());

            //3、对消费者和作者的余额和总支出/总收入进行更新数据库记录
            userOutlayService.save(consumerOutlay);
            userIncomeService.save(authorIncome);
            userBalanceService.updateBalanceByOrder(price, currentUser.getId(), byCnAndRi.getAuthorId());
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获得用户余额
     */
    @PostMapping("/gUBS")
    public Object getUserBalance(){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return userBalanceService.getUserBalance(currentUser.getId());
    }


}
