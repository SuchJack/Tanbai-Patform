import { http } from '@/utils/http'

/**
 * 获取随机参考回答
 */
export const getRandomReferencesAPI = (count: number = 5) => {
  return http({
    url: `/answer/reference/random?count=${count}`,
    method: 'GET',
  })
}
