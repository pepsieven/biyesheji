package com.pepsi.resources_share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pepsi.resources_share.entity.ReportRecord;

/**
 * @author pepsiL
 * @create 2020-05-06 3:29 下午
 */
public interface ReportResourceService extends IService<ReportRecord> {
    int report(String response,int uid,String reportReason);
}
