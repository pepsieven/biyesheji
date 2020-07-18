package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.OrderAlipay;

import java.util.List;

/**
 * @author pepsiL
 * @create 2020-03-01 12:29 下午
 */
public interface OrderAlipayService extends IService<OrderAlipay> {

    IPage<OrderAlipay> pageByUserid(Page page,int userid);

    List<OrderAlipay> listByUserid(int userid);

    int delOne(String alipayId);

}
