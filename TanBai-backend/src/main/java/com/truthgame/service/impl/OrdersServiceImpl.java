package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.mapper.OrderMapper;
import com.truthgame.model.entity.Orders;
import com.truthgame.service.OrdersService;
import com.truthgame.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author i7 12700KF
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2025-01-12 13:22:46
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Override
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询当前用户的订单
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>().eq("number", outTradeNo);
        Orders ordersDB = this.getOne(queryWrapper);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(2)
                .payStatus(1)
                .checkoutTime(LocalDateTime.now())
                .build();

        this.update(orders, queryWrapper);
    }
}




