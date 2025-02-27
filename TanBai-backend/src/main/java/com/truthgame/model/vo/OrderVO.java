package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单信息")
public class OrderVO {
    
    @ApiModelProperty("订单ID")
    private Long id;
    
    @ApiModelProperty("订单号")
    private String number;
    
    @ApiModelProperty("订单状态 1待付款 2已完成 3已取消 4退款")
    private Integer status;
    
    @ApiModelProperty("下单用户")
    private Long userId;
    
    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;
    
    @ApiModelProperty("结账时间")
    private LocalDateTime checkoutTime;
    
    @ApiModelProperty("支付方式 1微信,2支付宝")
    private Integer payMethod;
    
    @ApiModelProperty("支付状态 0未支付 1已支付 2退款")
    private Integer payStatus;
    
    @ApiModelProperty("实收金额")
    private BigDecimal amount;
} 