package com.truthgame.service;

import com.truthgame.model.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author i7 12700KF
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2025-01-12 13:22:46
*/
public interface OrdersService extends IService<Orders> {

    void paySuccess(String outTradeNo);
}
