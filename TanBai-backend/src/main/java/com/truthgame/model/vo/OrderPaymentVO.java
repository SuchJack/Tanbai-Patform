package com.truthgame.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "OrderPaymentVO", description = "订单支付信息")
public class OrderPaymentVO implements Serializable {

    private String nonceStr; //随机字符串
    private String paySign; //签名
    private String timeStamp; //时间戳
    private String signType; //签名算法
    private String packageStr; //统一下单接口返回的 prepay_id 参数值

}
