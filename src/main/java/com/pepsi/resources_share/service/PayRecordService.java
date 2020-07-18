package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.PayRecord;

/**
 * @author pepsiL
 * @create 2020-03-18 7:37 下午
 */
public interface PayRecordService extends IService<PayRecord> {

    PayRecord getOne(String alipayNumber);
}
