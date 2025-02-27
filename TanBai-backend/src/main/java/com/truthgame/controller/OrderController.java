package com.truthgame.controller;

import com.alibaba.fastjson.JSONObject;
import com.truthgame.common.Result;
import com.truthgame.exception.BusinessException;
import com.truthgame.model.dto.OrderDTO;
import com.truthgame.model.dto.PayDTO;
import com.truthgame.model.entity.Orders;
import com.truthgame.service.OrderService;
import com.truthgame.service.QuestionService;
import com.truthgame.utils.WeChatPayUtil;
import com.truthgame.model.vo.OrderPaymentVO;
import com.truthgame.model.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Api(tags = "订单管理接口")
public class OrderController {
    
    @Resource
    private OrderService orderService;

    @Resource
    private QuestionService questionService;
    
    @Resource
    private WeChatPayUtil weChatPayUtil;
    
    @PostMapping
    @ApiOperation("创建订单")
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return Result.success(orderService.createOrder(orderDTO));
    }
    
    @PostMapping("/pay")
    @ApiOperation("支付订单")
    public Result<OrderPaymentVO> payOrder(@Valid @RequestBody PayDTO payDTO) throws Exception {
        
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
        JSONObject jsonObject = weChatPayUtil.pay(
                payDTO.getOrderNumber(), //商户订单号
                new BigDecimal(5.20), //支付金额，单位 元
                "坦白驿站-解锁房间头像昵称", //商品描述
                payDTO.getOpenId() //微信用户的openid
        );
        System.out.println("jsonObject = " + jsonObject);

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new BusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return Result.success(vo);
    }

    @PostMapping("/pay/reply")
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
        JSONObject jsonObject = weChatPayUtil.pay(
                payDTO.getOrderNumber(), //商户订单号
                new BigDecimal(2.88), //支付金额，单位 元
                "坦白驿站-解锁房间回复", //商品描述
                payDTO.getOpenId() //微信用户的openid
        );
        System.out.println("jsonObject = " + jsonObject);

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new BusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return Result.success(vo);
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
    public Result<String> getOrderPayStatus(@PathVariable String orderNumber,@PathVariable Long questionId) {
        Orders order = orderService.getOrderByNumber(orderNumber);
        
        if (order == null) {
            return Result.success("FAIL");
        }
        
        // 根据支付状态返回结果
        if (order.getPayStatus() == 1) {
            // 更新问题支付状态
            questionService.update().set("is_paid", 1).eq("id", questionId).update();
            return Result.success("SUCCESS");
        } else if (order.getPayStatus() == 2) {
            return Result.success("REFUND");
        } else {
            return Result.success("PENDING");
        }
    }

    @GetMapping("/{orderNumber}/status/{questionId}/reply")
    @ApiOperation("查询订单支付状态")
    public Result<String> getReplyOrderPayStatus(@PathVariable String orderNumber,@PathVariable Long questionId) {
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
