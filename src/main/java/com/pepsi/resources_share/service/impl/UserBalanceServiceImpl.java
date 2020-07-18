package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.UserBalance;
import com.pepsi.resources_share.mapper.UserBalanceMapper;
import com.pepsi.resources_share.service.UserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@Service
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceMapper, UserBalance> implements UserBalanceService {
    @Autowired
    UserBalanceMapper userBalanceMapper;


    /**
     * 购买资源的逻辑支付业务
     * @param price
     * @param userid
     * @param authorid
     * @return
     */
    @Override
    public int updateBalanceByOrder(BigDecimal price, int userid, int authorid) {
        userBalanceMapper.updateConsumerBalance(price, userid);
        userBalanceMapper.updateAuthorBalance(price, authorid);
        return 1;
    }

    @Override
    public int removeBalanceByUserid(int userid) {
        int update = baseMapper.update(new UserBalance(),Wrappers.<UserBalance>lambdaUpdate().eq(UserBalance::getUserId, userid).set(UserBalance::getDisabled,1));
        return update;
    }

    /**
     * alipay沙盒支付的充值方法
     * @param price
     * @param userid
     * @return
     */
    @Override
    public int recharge(BigDecimal price, int userid) {
        return userBalanceMapper.updateBalance(price,userid);
    }

    @Override
    public BigDecimal getUserBalance(int userid) {
        return baseMapper.selectOne(Wrappers.<UserBalance>lambdaQuery().eq(UserBalance::getUserId,userid)).getBalance();
    }


}
