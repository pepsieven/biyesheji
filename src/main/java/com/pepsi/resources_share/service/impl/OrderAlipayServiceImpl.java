package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.OrderAlipay;
import com.pepsi.resources_share.mapper.OrderAlipayMapper;
import com.pepsi.resources_share.service.OrderAlipayService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 12:30 下午
 */
@Service
public class OrderAlipayServiceImpl extends ServiceImpl<OrderAlipayMapper, OrderAlipay> implements OrderAlipayService {
    @Override
    public IPage<OrderAlipay> pageByUserid(Page page, int userid) {
        return baseMapper.selectPage(page, Wrappers.<OrderAlipay>lambdaQuery().eq(OrderAlipay::getUserId, userid));
    }

    @Override
    public List<OrderAlipay> listByUserid(int userid) {
        return baseMapper.selectList(Wrappers.<OrderAlipay>lambdaQuery().eq(OrderAlipay::getUserId, userid));
    }

    @Override
    public int delOne(String alipayId) {

        return baseMapper.delete(Wrappers.<OrderAlipay>lambdaUpdate().eq(OrderAlipay::getAlipayId, alipayId));

    }


}
