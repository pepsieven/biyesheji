package com.pepsi.resources_share.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pepsi.resources_share.entity.ReportRecord;
import com.pepsi.resources_share.mapper.ReportResourceMapper;
import com.pepsi.resources_share.service.ReportResourceService;
import org.springframework.stereotype.Service;

/**
 * @author pepsiL
 * @create 2020-05-06 3:30 下午
 */
@Service
public class ReportResourceServiceImpl extends ServiceImpl<ReportResourceMapper, ReportRecord> implements ReportResourceService {

    @Override
    public int report(String response, int uid, String reportReason) {

        ReportRecord reportRecord = baseMapper.selectOne(Wrappers.<ReportRecord>lambdaQuery().eq(ReportRecord::getUid, uid).eq(ReportRecord::getResponse, response));
        if(reportRecord != null){
            return 0;
        }
        return baseMapper.insert(
                new ReportRecord().
                setUid(uid).
                setReportReason(reportReason).
                setResponse(response));
    }
}
