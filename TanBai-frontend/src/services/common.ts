import { http } from '@/utils/http'

/**
 * 获取常见问题
 * @param data
 */
export const getCommonQuestionAPI = () => {
  return http({
    url: '/qa',
    method: 'GET',
  })
}
