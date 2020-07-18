package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.PayRecord;
import com.pepsi.resources_share.mapper.PayRecordMapper;
import com.pepsi.resources_share.service.PayRecordService;
import org.springframework.stereotype.Service;

/**
 * @author pepsiL
 * @create 2020-03-18 7:37 下午
 */
@Service
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {
    @Override
    public PayRecord getOne(String alipayNumber) {
        return baseMapper.selectOne(Wrappers.<PayRecord>lambdaQuery().eq(PayRecord::getAlipayNumber,alipayNumber));
    }
}
