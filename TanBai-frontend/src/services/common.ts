import { http } from '../utils/http'

export const justTestAPI = () => {
  return http({
    url: '/test',
    method: 'GET',
  })
}

/**
 * 获取常见问题
 * @param data
 */
export const getCommonQuestionAPI = () => {
  return uni.request({
    url: '/qa',
    method: 'GET',
  })
}
