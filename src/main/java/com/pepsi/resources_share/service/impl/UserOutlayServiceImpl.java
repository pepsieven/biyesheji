package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pepsi.resources_share.entity.UserOutlay;
import com.pepsi.resources_share.mapper.UserOutlayMapper;
import com.pepsi.resources_share.service.UserOutlayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pepsi
 * @since 2020-02-27
 */
@Service
public class UserOutlayServiceImpl extends ServiceImpl<UserOutlayMapper, UserOutlay> implements UserOutlayService {

    @Override
    public int removeByUserid(int userid) {
        int update = baseMapper.update(new UserOutlay(), Wrappers.<UserOutlay>lambdaUpdate().eq(UserOutlay::getUserId, userid).set(UserOutlay::getDisabled, 1));
        return update;
    }

    @Override
    public Boolean confirmPayStatus(int userid, int orderid) {
        if(baseMapper.selectOne(Wrappers.<UserOutlay>lambdaQuery().eq(UserOutlay::getUserId,userid).eq(UserOutlay::getOrderId,orderid))!=null){
            return true;
        }
        return false;
    }
}
