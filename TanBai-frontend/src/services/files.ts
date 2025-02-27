import { http } from '../utils/http'
import { BACKEND_HOST_PROD } from '../constant'

export const justTestAPI = () => {
  return http({
    url: '/test',
    method: 'GET',
  })
}

/**
 * 上传文件
 * @param data
 */
export const uploadFileAPI = (data: { avatarUrl: any; userStore: any }) => {
  return uni.uploadFile({
    url: '/file/upload',
    filePath: data.avatarUrl,
    name: 'file',
    formData: {
      userId: data.userStore.profile?.id,
      biz: 'user_avatar',
      openId: data.userStore.profile?.openId,
    },
  })
}

/**
 * 下载二维码
 * @param data
 */
export const downloadQrCodeAPI = (data: { questionId: any; userStore: any }) => {
  return uni.downloadFile({
    url: BACKEND_HOST_PROD + `/questions/qrcode/${data.questionId}`,
    header: {
      Authorization: `Bearer ${data.userStore.profile?.token}`,
    },
  })
}

/**
 * 下载海报
 * @param data
 */
export const downloadPosterAPI = async (data: { questionId: any; userStore: any }) => {
  return uni.downloadFile({
    url: BACKEND_HOST_PROD + `/posters/generate/v1/${data.questionId}`,
    header: {
      Authorization: `Bearer ${data.userStore.profile?.token}`,
    },
  })
}

/**
 * 获取海报URL
 * @param data
 */
export const getPostURLAPI = async (data: { questionId: any; userStore: any }) => {
  return uni.request({
    url: BACKEND_HOST_PROD + `/posters/generate/v2/${data.questionId}`,
    header: {
      Authorization: `Bearer ${data.userStore.profile?.token}`,
    },
  })
}
