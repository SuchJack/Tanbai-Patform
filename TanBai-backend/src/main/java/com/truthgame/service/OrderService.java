package com.truthgame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.truthgame.model.entity.Orders;
import com.truthgame.model.dto.OrderDTO;
import com.truthgame.model.vo.OrderVO;
import java.util.List;

public interface OrderService extends IService<Orders> {
    
    /**
     * 创建订单
     */
    OrderVO createOrder(OrderDTO orderDTO);
    
    /**
     * 支付订单
     */
    boolean payOrder(String orderNumber, Long userId);
    
    /**
     * 取消订单
     */
    boolean cancelOrder(String orderNumber, Long userId);
    
    /**
     * 退款
     */
    boolean refundOrder(String orderNumber, Long userId);
    
    /**
     * 获取用户订单列表
     */
    List<OrderVO> getUserOrders(Long userId);
    
    /**
     * 获取订单详情
     */
    OrderVO getOrderDetail(String orderNumber, Long userId);
    
    /**
     * 根据订单号获取订单
     */
    Orders getOrderByNumber(String orderNumber);
} 