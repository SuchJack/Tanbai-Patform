package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@ApiModel(description = "订单创建请求")
public class OrderDTO {
    
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    @ApiModelProperty("支付金额")
    private BigDecimal amount;
    
    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty("支付方式 1微信,2支付宝")
    private Integer payMethod;
} 