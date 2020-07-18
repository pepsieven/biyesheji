package com.pepsi.resources_share.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.Order;
import com.pepsi.resources_share.service.OrderService;
import com.pepsi.resources_share.utils.OrderNumberUtils;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 12:32 下午
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 根据用户点击所想付款的资源进行订单详细信息创建并返回
     * 然后前端接收此订单再进行付款操作
     * 若取消付款，则将此方法进行回滚
     *
     * @return
     */
    @PostMapping("/i")
    public boolean insertOne(Order order) {

        // 获得当前下单的用户信息
        CurrentUser currentUser = SecurityUtils.getCurrentUser();

        Order detectionOrder = orderService.getByCnAndRi(currentUser.getUsername(), order.getResourceId());
        if (detectionOrder != null) {
            return false;
        } else {
            order.setConsumerId(currentUser.getId());
            order.setConsumerName(currentUser.getUsername());
            //生成订单号
            long userId = currentUser.getId().longValue();
            OrderNumberUtils orderNumberUtils = new OrderNumberUtils();
            String orderCode = orderNumberUtils.getOrderCode(userId);
            order.setOrderNumber(orderCode);
            return orderService.save(order);

        }
    }

    /**
     * 查看每个用户消费所属的订单
     *
     * @return
     */
    @PostMapping("/gc")
    public List<Order> listConsumerByUid() {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        //1、查得身为消费者的订单信息
        return orderService.list(new QueryWrapper<Order>().eq("consumer_id", currentUser.getId()));


    }

    /**
     * 获得作者的收入订单信息
     *
     * @return
     */
    @PostMapping("/ga")
    public List<Order> listAuthorByUid() {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        //1、查得身为作者的订单信息
        return orderService.list(new QueryWrapper<Order>().eq("author_id", currentUser.getId()));
    }

    /**
     * 管理员更新一条订单
     *
     * @param order
     * @return
     */
    public Object updateOne(Order order) {
        return orderService.updateById(order);
    }

    /**
     * 删除一条订单
     *
     * @param order
     * @return
     */
    @PostMapping("/d")
    public Object deleteOne(Order order) {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        return orderService.cancel(currentUser.getUsername(), order.getResourceId());
    }

    @PostMapping("/fn")
    public List<Order> getByON(@RequestParam String orderNumber) {
        return orderService.getByOrderNumber(orderNumber);
    }

    /**
     * 查看所有订单记录
     * @return
     */
    @PostMapping("/mga")
    public List<Order> getAllOrder(){
        return orderService.list();
    }


}
