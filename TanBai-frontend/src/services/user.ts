import { http } from '@/utils/http'

/**
 * 获取用户登录信息
 * @param data
 */
export const getUserLoginAPI = (data: { code: any }) => {
  return http({
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
  return http({
    url: '/users/info',
    method: 'PUT',
    data,
  })
}

/**
 * 获取当前登录用户信息
 */
export const getLoginUserVOAPI = () => {
  return http({
    url: '/auth/get/login',
    method: 'GET',
  })
}
