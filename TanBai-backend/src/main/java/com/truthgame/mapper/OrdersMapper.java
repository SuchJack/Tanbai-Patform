package com.truthgame.mapper;

import com.truthgame.model.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author i7 12700KF
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2025-01-12 13:22:46
* @Entity com.truthgame.model.entity.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




