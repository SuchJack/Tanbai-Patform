import { http } from '@/utils/http'

interface CustomerService {
  id: number
  wechat: string
  email: string
  workingHours: string
}

/**
 * 获取客服信息
 */
export const getCustomerServiceInfo = () => {
  return http<CustomerService>({
    method: 'GET',
    url: '/customer-service',
  })
}
