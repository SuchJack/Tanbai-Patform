import { http } from '../utils/http'

export const justTestAPI = () => {
  return http({
    url: '/test',
    method: 'GET',
  })
}

/**
 * 创建订单
 * @param data
 */
export const createOrderAPI = (data: { userStore: any }) => {
  return uni.request({
    url: '/orders',
    method: 'POST',
    data: {
      userId: data.userStore.profile?.id,
      amount: 5.2,
      payMethod: 1, // 1:微信支付
    },
    header: {
      'Content-Type': 'application/json',
    },
  })
}

/**
 * 发起支付
 * @param data
 */
export const reqPayAPI = (data: { orderNumber: any; userStore: any }) => {
  return uni.request({
    url: `/orders/pay`,
    method: 'POST',
    data: {
      orderNumber: data.orderNumber,
      userId: data.userStore.profile?.id,
      openId: data.userStore.profile?.openId,
    },
  })
}

/**
 * 获取订单状态
 * @param data
 */
export const getOrderStatusAPI = (data: { orderNumber: any; questionId: any }) => {
  return uni.request({
    url: `/orders/${data.orderNumber}/status/${data.questionId}`,
    method: 'GET',
  })
}

/**
 * 发起Reply支付
 * @param data
 */
export const reqReplyPayAPI = (data: { orderNumber: any; userStore: any }) => {
  return uni.request({
    url: `/orders/pay/reply`,
    method: 'POST',
    data: {
      orderNumber: data.orderNumber,
      userId: data.userStore.profile?.id,
      openId: data.userStore.profile?.openId,
    },
  })
}

/**
 * 获取Reply订单状态
 * @param data
 */
export const getReplyOrderStatusAPI = (data: { orderNumber: any; questionId: any }) => {
  return uni.request({
    url: `/orders/${data.orderNumber}/status/${data.questionId}/reply`,
    method: 'GET',
  })
}
