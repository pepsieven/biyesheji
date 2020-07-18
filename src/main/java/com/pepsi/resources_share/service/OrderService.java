package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.Order;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 11:31 上午
 */


public interface OrderService extends IService<Order> {

    List<Order> getByOrderNumber(String orderNumber);

    Order getByCnAndRi(String consumerName,String resourceId);

    Object cancel(String consumerName,String resourceId);

}
