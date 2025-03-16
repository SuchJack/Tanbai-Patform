import { http } from '@/utils/http'
import { BACKEND_HOST_PROD } from '@/constant'

/**
 * 上传文件
 * @param data
 */
export const uploadFileAPI = (data: { avatarUrl: any; userStore: any }) => {
  return uni.uploadFile({
    url: BACKEND_HOST_PROD + '/file/upload',
    filePath: data.avatarUrl,
    name: 'file',
    formData: {
      userId: data.userStore.profile?.userId,
      biz: 'user_avatar',
      openId: data.userStore.profile?.openId,
    },
  })
}

/**
 * 获取海报Base64
 * @param data
 */
export const getPostBase64 = async (data: { questionId: any }) => {
  return http({
    url: `/posters/generate/v3/${data.questionId}`,
    method: 'GET',
  })
}
