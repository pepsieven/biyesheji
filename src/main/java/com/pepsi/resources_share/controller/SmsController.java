package com.pepsi.resources_share.controller;

import com.pepsi.resources_share.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pepsiL
 * @create 2020-03-23 1:19 下午
 */
@RestController
@RequestMapping("/Sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    /**
     *
     * @param phone
     * @return
     */
    @PostMapping("/cdo")
    public String  getcode(@RequestParam String phone){
        long l=System.currentTimeMillis();
        int k1=(int) (l%10000);
        String code=String.valueOf(k1);
        //成功返回0，失败返回1
        if(phone!=null){
            //把后台生成的code和所发送的手机号传进发送消息类，调用执行。
            smsService.send(phone,code);
            return code;
        }
        return null;
    }

}
