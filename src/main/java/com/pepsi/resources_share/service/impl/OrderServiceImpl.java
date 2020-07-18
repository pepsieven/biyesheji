package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.Order;
import com.pepsi.resources_share.mapper.OrderMapper;
import com.pepsi.resources_share.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 11:32 上午
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     * @return
     */
    @Override
    public List<Order> getByOrderNumber(String orderNumber) {
        return baseMapper.selectList(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNumber, orderNumber));
    }

    @Override
    public Order getByCnAndRi(String consumerName, String resourceId) {
        return baseMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getConsumerName,consumerName).eq(Order::getResourceId,resourceId));
    }

    @Override
    public Object cancel(String consumerName, String resourceId) {
        return baseMapper.delete(Wrappers.<Order>lambdaUpdate().eq(Order::getConsumerName, consumerName).eq(Order::getResourceId, resourceId));
    }


}
