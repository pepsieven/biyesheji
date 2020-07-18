package com.pepsi.resources_share.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.pepsi.resources_share.config.AlipayConfig;
import com.pepsi.resources_share.entity.CurrentUser;
import com.pepsi.resources_share.entity.OrderAlipay;
import com.pepsi.resources_share.entity.PayRecord;
import com.pepsi.resources_share.service.OrderAlipayService;
import com.pepsi.resources_share.service.PayRecordService;
import com.pepsi.resources_share.service.UserBalanceService;
import com.pepsi.resources_share.service.UserService;
import com.pepsi.resources_share.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝沙盒测试的代码
 * @author pepsiL
 * @create 2020-02-29 12:49 下午
 */
@RestController
@RequestMapping("/alipay")
public class AlipayController {

    private final UserService userService;

    private final UserBalanceService userBalanceService;

    private final OrderAlipayService orderAlipayService;

    private final PayRecordService payRecordService;

    public AlipayController(UserService userService, UserBalanceService userBalanceService, OrderAlipayService orderAlipayService, PayRecordService payRecordService) {
        this.userService = userService;
        this.userBalanceService = userBalanceService;
        this.orderAlipayService = orderAlipayService;
        this.payRecordService = payRecordService;
    }


    @RequestMapping(value = "/goAlipay", produces = "text/html; charset=UTF-8")
    public String goAlipay(HttpServletRequest request,HttpServletResponse response) throws Exception {

        response.setHeader("Authorization",request.getHeader("Authorization"));
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //付款金额，必填
        String total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
        //订单名称，必填
        String subject = "充值金币";
        //商品描述，可空
        String body = "充值金币";
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "10m";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout_express +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //3、开一个新的记录以存储跳转的信息
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        PayRecord payRecord = new PayRecord();
        payRecord.setUserId(currentUser.getId());
        payRecord.setAlipayNumber(out_trade_no);
        payRecordService.save(payRecord);
        //4、请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }


    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,String> params = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if(signVerified) {
            //验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = "TRADE_SUCCESS";

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                System.out.println("TRADE_FINISHED");
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                //
                PayRecord one = payRecordService.getOne(out_trade_no);

                // 修改用户余额
                HttpSession session = request.getSession();
                BigDecimal money = new BigDecimal(total_amount);
                userBalanceService.recharge(money,one.getUserId());

                //  保存充值记录
                OrderAlipay orderAlipay = new OrderAlipay();
                orderAlipay.setUserId(one.getUserId());
                orderAlipay.setAlipayPrice(money);
                orderAlipay.setAlipayId(out_trade_no);
                orderAlipayService.save(orderAlipay);
            }

        }else {//验证失败
            System.out.println("验证失败");
        }
        response.sendRedirect("http://localhost:8080/#/paysuccess");
        return "ok";
    }


    @RequestMapping("/notifyUrl")
    @ResponseBody
    public String notifyUrl(HttpServletRequest request) throws Exception {
        System.out.println("异步请求");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//					valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
         /* 实际验证过程建议商户务必添加以下校验：
				1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
				2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
				3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
				4、验证app_id是否为该商户本身。
				*/
        if (signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            if (trade_status.equals("TRADE_FINISHED")) {
                System.out.println("TRADE_FINISHED");
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
                System.out.println("TRADE_SUCCESS");
                HttpSession session = request.getSession();
                BigDecimal money = new BigDecimal(total_amount);
                // 修改用户余额
                CurrentUser currentUser = SecurityUtils.getCurrentUser();
                userBalanceService.recharge(money,currentUser.getId());

                //  保存充值记录
                OrderAlipay orderAlipay = new OrderAlipay();
                orderAlipay.setUserId(currentUser.getId());
                orderAlipay.setAlipayPrice(money);
                orderAlipayService.save(orderAlipay);

            }
        }else {//验证失败
                System.out.println("验证失败");
            }
        return "success";
    }

}
