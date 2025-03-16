package com.truthgame.constant;

public class OrderStatus {
    // 订单状态
    public static final int PENDING = 1;  // 待付款
    public static final int COMPLETED = 2; // 已完成
    public static final int CANCELLED = 3; // 已取消
    public static final int REFUNDED = 4;  // 退款
    
    // 支付状态
    public static final int UNPAID = 0;    // 未支付
    public static final int PAID = 1;      // 已支付
    public static final int REFUND = 2;    // 退款
} 