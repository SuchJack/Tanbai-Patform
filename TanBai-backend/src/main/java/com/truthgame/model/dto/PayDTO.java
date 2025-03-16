package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "支付创建请求")
public class PayDTO {

    @NotNull(message = "订单号不能为空")
    @ApiModelProperty("订单号")
    String orderNumber;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    String userId;

    @NotNull(message = "用户用户openId不能为空")
    @ApiModelProperty("用户openId")
    String openId;
} 