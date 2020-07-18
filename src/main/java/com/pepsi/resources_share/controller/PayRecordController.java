package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.service.PayRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pepsiL
 * @create 2020-03-18 7:47 下午
 */
@RestController
@RequestMapping("/payrecord")
public class PayRecordController {
    private final PayRecordService recordService;

    public PayRecordController(PayRecordService recordService) {
        this.recordService = recordService;
    }
}
