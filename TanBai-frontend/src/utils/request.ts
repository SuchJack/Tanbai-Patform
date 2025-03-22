import { COOKIE_KEY } from '@/constant'
import { useUserStore } from '@/stores'

export default async function request(url: any, options: any = {}) {
  return new Promise((resolve, reject) => {
    const {
      method = 'GET',
      headers = {},
      data = {},
      timeout,
      withCredentials,
      ...otherOptions
    } = options

    uni.request({
      url,
      method,
      header: headers,
      data,
      timeout,
      withCredentials, // 用于跨域请求时是否携带凭证
      ...otherOptions,
      success: (res: any) => {
        console.log('响应拦截器触发')
        // 处理响应中的 Set-Cookie 头
        const setCookie = res.header['Set-Cookie'] || res.header['set-cookie']
        if (setCookie) {
          uni.setStorageSync(COOKIE_KEY, setCookie)
        }
        console.log('响应数据', res)
        // 构造符合 uniapp 的响应对象
        const response = {
          data: res.data,
          status: res.statusCode,
          statusText: res.errMsg,
          headers: res.header,
          config: options,
          request: res,
        }
        // 根据 HTTP 状态码判断请求是否成功
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(response.data)
        } else {
          // 401错误  -> 清理用户信息，跳转到登录页
          const userStore = useUserStore()
          userStore.clearProfile()
          uni.showToast({
            icon: 'none',
            title: (res.data as any).msg || '请求错误',
          })
          reject(response)
        }
      },
      fail: (error: any) => {
        uni.showToast({
          icon: 'none',
          title: '网络错误，换个网络试试',
        })
        // 构造符合 uniapp 错误格式的对象
        const err = {
          message: error.errMsg || 'Request failed',
          config: options,
          request: error,
        }
        reject(err)
      },
    })
  })
}
