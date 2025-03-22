/**
 * 添加拦截器:
 *   拦截 request 请求
 *   拦截 uploadFile 文件上传
 *
 * TODO:
 *   1. 非 http 开头需拼接地址
 *   2. 请求超时
 *   3. 添加小程序端请求头标识
 *   4. 添加 token 请求头标识
 */
import { useMemberStore, useUserStore } from '@/stores'
import { BACKEND_HOST_PROD, COOKIE_KEY } from '@/constant'

// 请求基地址
const baseURL = BACKEND_HOST_PROD

// 拦截器配置
const httpInterceptor = {
  // 拦截前触发
  invoke(options: UniApp.RequestOptions) {
    console.log('请求拦截器触发')
    // 1. 非 http 开头需拼接地址
    if (!options.url.startsWith('http')) {
      options.url = baseURL + options.url
    }
    // 2. 请求超时
    options.timeout = 10000
    // 3. 添加小程序端请求头标识
    options.header = {
      'source-client': 'miniapp',
      ...options.header,
    }
    // 4. 添加 token 请求头标识
    const userStore = useUserStore()
    const token = userStore.profile?.tokenValue
    if (token) {
      options.header.Authorization = token
    }

    // 5. 添加 Cookie 到请求头
    const cookie = uni.getStorageSync(COOKIE_KEY)
    if (cookie) {
      options.header.cookie = cookie
    }
  },
}

// 拦截 request 请求
uni.addInterceptor('request', httpInterceptor)
// 拦截 uploadFile 文件上传
uni.addInterceptor('uploadFile', httpInterceptor)

/**
 * 请求函数(基于uni-app封装request)
 * @param  {string} url 请求地址
 * @param  {object} options 请求参数
 * @returns Promise
 *  1. 返回 Promise 对象，用于处理返回值类型
 *  2. 获取数据成功
 *    2.1 提取核心数据 res.data
 *    2.2 添加类型，支持泛型
 *  3. 获取数据失败
 *    3.1 401错误  -> 清理用户信息，跳转到登录页
 *    3.2 其他错误 -> 根据后端错误信息轻提示
 *    3.3 网络错误 -> 提示用户换网络
 */
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
        // 1. 处理响应中的 Set-Cookie 头
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
        // 2. 根据 HTTP 状态码判断请求是否成功
        if (res.statusCode >= 200 && res.statusCode < 300) {
          // 2.1 提取核心数据 res.data
          resolve(response.data)
        } else if (res.statusCode === 401) {
          // 2.1 401错误  -> 清理用户信息，跳转到登录页
          const memberStore = useMemberStore()
          memberStore.clearProfile()
          // uni.navigateTo({ url: '/pages/login/login' })
          reject(res)
        } else {
          // 2.2 其他错误 -> 根据后端错误信息轻提示
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
