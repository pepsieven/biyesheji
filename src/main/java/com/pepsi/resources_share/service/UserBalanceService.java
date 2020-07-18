package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.UserBalance;

import java.math.BigDecimal;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
public interface UserBalanceService extends IService<UserBalance> {

    /**
     * 自定义的更新用户余额方法
     *
     * @param price
     * @param userid
     * @return
     */
    int updateBalanceByOrder(BigDecimal price, int userid, int authorid);

    int removeBalanceByUserid(int userid);

    int recharge(BigDecimal price,int userid);

    BigDecimal getUserBalance(int userid);


}
