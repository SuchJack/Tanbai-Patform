package com.truthgame.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.truthgame.service.OrdersService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
@Slf4j
public class PayNotifyController {
    @Autowired
    private OrdersService ordersService;
    @Resource
    private WxPayService wxService;

    /**
     * <pre>
     * 支付成功回调
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791861
     * </pre>
     *
     * @param notifyData
     * @param request
     * @return
     * @throws WxPayException
     */
    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/paySuccess")
    public ResponseEntity<String> parseOrderNotifyResult(@RequestBody String notifyData, HttpServletRequest request) {
        try {
            WxPayNotifyV3Result res = this.wxService.parseOrderNotifyV3Result(notifyData, null);
            WxPayNotifyV3Result.DecryptNotifyResult decryptRes = res.getResult();
            System.out.println("decryptRes = " + decryptRes);
            // TODO 根据自己业务场景需要构造返回对象
            if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(decryptRes.getTradeState())) {
                // 业务处理，修改订单状态
                ordersService.paySuccess(decryptRes.getOutTradeNo());
                //成功返回200/204，body无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                //失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            //失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        }
    }

    /**
     * <pre>
     * 退款成功回调
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791865
     * </pre>
     *
     * @param notifyData
     * @param request
     * @return
     * @throws WxPayException
     */
    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/refund")
    public ResponseEntity<String> parseRefundNotifyResult(@RequestBody String notifyData, HttpServletRequest request) {
        try {
            WxPayRefundNotifyV3Result res = this.wxService.parseRefundNotifyV3Result(notifyData, null);
            WxPayRefundNotifyV3Result.DecryptNotifyResult decryptRes = res.getResult();
            System.out.println("decryptRes = " + decryptRes);
            // TODO 根据自己业务场景需要构造返回对象
            if (WxPayConstants.RefundStatus.SUCCESS.equals(decryptRes.getRefundStatus())) {
                //成功返回200/204，body无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                //失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            //失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        }
    }

}
