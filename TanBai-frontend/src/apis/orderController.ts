/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** 创建订单 POST /orders */
export async function createOrderUsingPost({
  body,
  options,
}: {
  body: API.OrderDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultOrderVO_>('/orders', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取订单详情 GET /orders/${param0} */
export async function getOrderDetailUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getOrderDetailUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { orderNumber: param0, ...queryParams } = params;

  return request<API.ResultOrderVO_>(`/orders/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 取消订单 POST /orders/${param0}/cancel */
export async function cancelOrderUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.cancelOrderUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  const { orderNumber: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/orders/${param0}/cancel`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 申请退款 POST /orders/${param0}/refund */
export async function refundOrderUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.refundOrderUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  const { orderNumber: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/orders/${param0}/refund`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 查询订单支付状态 GET /orders/${param0}/status/${param1} */
export async function getOrderPayStatusUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getOrderPayStatusUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { orderNumber: param0, questionId: param1, ...queryParams } = params;

  return request<API.ResultString_>(`/orders/${param0}/status/${param1}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 查询订单支付状态 GET /orders/${param0}/status/${param1}/reply */
export async function getReplyOrderPayStatusUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getReplyOrderPayStatusUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { orderNumber: param0, questionId: param1, ...queryParams } = params;

  return request<API.ResultString_>(
    `/orders/${param0}/status/${param1}/reply`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 支付订单 POST /orders/pay */
export async function payOrderUsingPost({
  body,
  options,
}: {
  body: API.PayDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultOrderPaymentVO_>('/orders/pay', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 支付Reply订单 POST /orders/pay/reply */
export async function payReplyOrderUsingPost({
  body,
  options,
}: {
  body: API.PayDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultOrderPaymentVO_>('/orders/pay/reply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户订单列表 GET /orders/user/${param0} */
export async function getUserOrdersUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getUserOrdersUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { userId: param0, ...queryParams } = params;

  return request<API.ResultListOrderVO_>(`/orders/user/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
