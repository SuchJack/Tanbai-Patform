package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.OrderMapper;
import com.truthgame.model.entity.Orders;
import com.truthgame.model.dto.OrderDTO;
import com.truthgame.service.OrderService;
import com.truthgame.model.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    
    @Resource
    private OrderMapper orderMapper;
    
    @Override
    public OrderVO createOrder(OrderDTO orderDTO) {
        // 1. 生成订单号
        String orderNumber = generateOrderNumber();
        
        // 2. 创建订单
        Orders order = new Orders();
        order.setNumber(orderNumber);
        order.setUserId(orderDTO.getUserId());
        order.setAmount(orderDTO.getAmount());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(1); // 待付款
        order.setPayStatus(0); // 未支付
        order.setPayMethod(orderDTO.getPayMethod());
        
        // 3. 保存订单
        save(order);
        
        // 4. 返回订单信息
        return convertToVO(order);
    }
    
    @Override
    public boolean payOrder(String orderNumber, Long userId) {
        // 1. 查询订单
        Orders order = getOrderByNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 2. 验证订单所属人
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        
        // 3. 验证订单状态
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }
        
        // 4. 更新订单状态
        order.setStatus(2); // 已完成
        order.setPayStatus(1); // 已支付
        order.setCheckoutTime(LocalDateTime.now());
        
        return updateById(order);
    }
    
    @Override
    public boolean cancelOrder(String orderNumber, Long userId) {
        Orders order = getOrderByNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        
        if (order.getStatus() != 1) {
            throw new BusinessException("只能取消待付款订单");
        }
        
        order.setStatus(3); // 已取消
        return updateById(order);
    }
    
    @Override
    public boolean refundOrder(String orderNumber, Long userId) {
        Orders order = getOrderByNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        
        if (order.getStatus() != 2 || order.getPayStatus() != 1) {
            throw new BusinessException("订单状态不正确");
        }
        
        order.setStatus(4); // 退款
        order.setPayStatus(2); // 退款
        return updateById(order);
    }
    
    @Override
    public List<OrderVO> getUserOrders(Long userId) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId)
               .orderByDesc(Orders::getOrderTime);
        
        List<Orders> orders = list(wrapper);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public OrderVO getOrderDetail(String orderNumber, Long userId) {
        Orders order = getOrderByNumber(orderNumber);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权查看此订单");
        }
        
        return convertToVO(order);
    }
    
    @Override
    public Orders getOrderByNumber(String orderNumber) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getNumber, orderNumber);
        return getOne(wrapper);
    }
    
    private String generateOrderNumber() {
        // 生成订单号：时间戳 + UUID前8位
        return System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
    
    private OrderVO convertToVO(Orders order) {
        OrderVO vo = new OrderVO();
        // 复制属性
        BeanUtils.copyProperties(order, vo);
        return vo;
    }
} 