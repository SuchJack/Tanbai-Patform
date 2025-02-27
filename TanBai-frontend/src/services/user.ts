import { http } from '../utils/http'

export const justTestAPI = () => {
  return http({
    url: '/test',
    method: 'GET',
  })
}

/**
 * 获取用户登录信息
 * @param data
 */
export const getUserLoginAPI = (data: { code: any }) => {
  return uni.request({
    url: '/auth/wx/login',
    method: 'POST',
    data: {
      code: data.code,
    },
  })
}

/**
 * 更新用户信息
 * @param data
 */
export const updateUserProfileAPI = (data: { userId: any; nickName: any; avatarUrl: any }) => {
  return uni.request({
    url: '/users/info',
    method: 'PUT',
    data,
  })
}
