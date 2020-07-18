package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.OrderAlipay;
import com.pepsi.resources_share.service.OrderAlipayService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 12:32 下午
 */

@RestController
@RequestMapping("/order-alipay")
public class OrderAlipayController {

    private final OrderAlipayService orderAlipayService;

    public OrderAlipayController(OrderAlipayService orderAlipayService) {
        this.orderAlipayService = orderAlipayService;
    }

    /**
     * 获得支付宝记录
     *
     * @return
     */
    @GetMapping("/goAll")
    public List<OrderAlipay> pageByUser() {
        return orderAlipayService.list();
    }

    /**
     * 获得指定用户的充值记录
     * @return
     */
    @GetMapping("/gi")
    public List<OrderAlipay> listAllById(){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return orderAlipayService.listByUserid(currentUser.getId());
    }

    /**
     * 删除一条记录，软删除并返回删除此记录后的list给前台
     * @return
     */
    @PostMapping("/do")
    public List<OrderAlipay> delOne(String alipayId){
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        orderAlipayService.delOne(alipayId);
        return orderAlipayService.listByUserid(currentUser.getId());
    }


}
