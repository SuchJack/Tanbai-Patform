package com.truthgame.controller;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import com.truthgame.common.Result;
import com.truthgame.exception.BusinessException;
import com.truthgame.model.dto.OrderDTO;
import com.truthgame.model.dto.PayDTO;
import com.truthgame.model.entity.Orders;
import com.truthgame.model.vo.OrderPaymentVO;
import com.truthgame.model.vo.OrderVO;
import com.truthgame.service.OrderService;
import com.truthgame.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private QuestionService questionService;
    @Resource
    private WxPayService wxService;

    @PostMapping
    @ApiOperation("创建订单")
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return Result.success(orderService.createOrder(orderDTO));
    }

    /**
     * 调用统一下单接口(JSAPI)
     * 详见：https://pay.weixin.qq.com/doc/v3/merchant/4012791856
     */
    @PostMapping("/pay/v3")
    @ApiOperation("支付订单")
    public Result<OrderPaymentVO> payOrderV3(@Valid @RequestBody PayDTO payDTO) throws Exception {

        // 1. 查询订单
        Orders order = orderService.getOrderByNumber(payDTO.getOrderNumber());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证订单所属人
        if (!order.getUserId().toString().equals(payDTO.getUserId())) {
            throw new BusinessException("无权操作此订单");
        }

        // 3. 调用微信支付
        //调用微信支付接口，生成预支付交易单
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request(); // appid mchid notifyurl 已自动注入
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(520);
        amount.setCurrency("CNY");
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(payDTO.getOpenId());
        request.setOutTradeNo(payDTO.getOrderNumber());
        request.setDescription("坦白驿站-解锁房间头像昵称");
        request.setAmount(amount);
        request.setPayer(payer);
        WxPayUnifiedOrderV3Result.JsapiResult result = wxService.createOrderV3(TradeTypeEnum.JSAPI, request);
        System.out.println("【调用支付结果】result = " + result);

        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        BeanUtils.copyProperties(result, orderPaymentVO);
        orderPaymentVO.setPackageStr(result.getPackageValue());
        return Result.success(orderPaymentVO);
    }

    @PostMapping("/pay/reply/v3")
    @ApiOperation("支付Reply订单")
    public Result<OrderPaymentVO> payReplyOrder(@Valid @RequestBody PayDTO payDTO) throws Exception {

        // 1. 查询订单
        Orders order = orderService.getOrderByNumber(payDTO.getOrderNumber());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证订单所属人
        if (!order.getUserId().toString().equals(payDTO.getUserId())) {
            throw new BusinessException("无权操作此订单");
        }

        // 3. 调用微信支付
        //调用微信支付接口，生成预支付交易单
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request(); // appid mchid 已自动注入
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(520);
        amount.setCurrency("CNY");
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(payDTO.getOpenId());
        request.setOutTradeNo(payDTO.getOrderNumber());
        request.setDescription("坦白驿站-解锁房间头像昵称");
        request.setAmount(amount);
        request.setPayer(payer);
        request.setNotifyUrl("https://1jyia03853275.vicp.fun/notify/paySuccess");
        WxPayUnifiedOrderV3Result.JsapiResult result = wxService.createOrderV3(TradeTypeEnum.JSAPI, request);
        System.out.println("【调用支付结果】result = " + result);

        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        BeanUtils.copyProperties(result, orderPaymentVO);
        orderPaymentVO.setPackageStr(result.getPackageValue());
        return Result.success(orderPaymentVO);
    }

    @PostMapping("/{orderNumber}/cancel")
    @ApiOperation("取消订单")
    public Result<Boolean> cancelOrder(
            @ApiParam("订单号") @PathVariable String orderNumber,
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(orderService.cancelOrder(orderNumber, userId));
    }

    @PostMapping("/{orderNumber}/refund")
    @ApiOperation("申请退款")
    public Result<Boolean> refundOrder(
            @ApiParam("订单号") @PathVariable String orderNumber,
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(orderService.refundOrder(orderNumber, userId));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户订单列表")
    public Result<List<OrderVO>> getUserOrders(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @GetMapping("/{orderNumber}")
    @ApiOperation("获取订单详情")
    public Result<OrderVO> getOrderDetail(
            @ApiParam("订单号") @PathVariable String orderNumber,
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(orderService.getOrderDetail(orderNumber, userId));
    }

    @GetMapping("/{orderNumber}/status/{questionId}")
    @ApiOperation("查询订单支付状态")
    public Result<String> getOrderPayStatus(@PathVariable String orderNumber, @PathVariable Long questionId) {
        Orders order = orderService.getOrderByNumber(orderNumber);

        if (order == null) {
            return Result.success("FAIL");
        }

        // 根据支付状态返回结果
        if (order.getPayStatus() == 1) {
            // 更新问题支付状态
            questionService.update().set("isPaid", 1).eq("id", questionId).update();
            log.info("更新问题支付状态成功");
            return Result.success("SUCCESS");
        } else if (order.getPayStatus() == 2) {
            return Result.success("REFUND");
        } else {
            return Result.success("PENDING");
        }
    }

    @GetMapping("/{orderNumber}/status/{questionId}/reply")
    @ApiOperation("查询订单支付状态")
    public Result<String> getReplyOrderPayStatus(@PathVariable String orderNumber, @PathVariable Long questionId) {
        Orders order = orderService.getOrderByNumber(orderNumber);

        if (order == null) {
            return Result.success("FAIL");
        }

        // 根据支付状态返回结果
        if (order.getPayStatus() == 1) {
            // 更新Reply支付状态
            questionService.payForViewReplyPermission(questionId, order.getUserId());
            return Result.success("SUCCESS");
        } else if (order.getPayStatus() == 2) {
            return Result.success("REFUND");
        } else {
            return Result.success("PENDING");
        }
    }
}
