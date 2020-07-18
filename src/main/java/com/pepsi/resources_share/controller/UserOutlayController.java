package com.pepsi.resources_share.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.Order;
import com.pepsi.resources_share.entity.User;
import com.pepsi.resources_share.entity.UserOutlay;
import com.pepsi.resources_share.service.OrderService;
import com.pepsi.resources_share.service.UserOutlayService;
import com.pepsi.resources_share.service.UserService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/user-outlay")
public class UserOutlayController {

    private final UserOutlayService userOutlayService;

    private final UserService userService;

    private final OrderService orderService;

    public UserOutlayController(UserOutlayService userOutlayService, UserService userService, OrderService orderService) {
        this.userOutlayService = userOutlayService;
        this.userService = userService;
        this.orderService = orderService;
    }


    /**
     * 管理员和用户自身查看支出记录皆可调用
     * 根据所选用户进行分页查询其支出记录
     *
     * @return
     */
    @RequestMapping("/pageListByUid")
    public Object pageListByUid(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam("userName") String userName) {
        //先根据用户名查得对应id
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_name", userName);
        User one = userService.getOne(queryWrapper);

        //分页查询对应用户的总收入记录
        QueryWrapper<UserOutlay> qw = new QueryWrapper<>();
        qw
                .eq("user_id", one.getId());
        Page p = new Page(page, 6);
        return userOutlayService.page(p, qw);

    }

    /**
     * 下单后页面刷新时检测订单是否付款
     *
     * @return
     */
    @PostMapping("/cPS")
    public Boolean confirmPayStatus(@RequestParam String resourceId) {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        Order byCnAndRi = orderService.getByCnAndRi(currentUser.getUsername(), resourceId);
        if (byCnAndRi != null) {
            return userOutlayService.confirmPayStatus(currentUser.getId(), byCnAndRi.getId());
        }
        return true;
    }

}
