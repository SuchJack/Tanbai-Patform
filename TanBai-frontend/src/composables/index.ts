import { useUserStore } from '@/stores'
import { getLoginUserVoUsingGet, wxLoginUsingPost } from '@/apis'

const userStore = useUserStore()

/**
 * 处理 code 和 登录
 */
export const handleCodeAndLogin = async (userLoginCache: any) => {
  // 如果已经在登录中，直接返回
  if (userStore.loginState.isLoggingIn) {
    return false
  }

  // 设置登录状态为登录中
  userStore.setLoginState({ isLoggingIn: true, isLoggedIn: false, loginError: null })

  // 先尝试获取当前登录用户信息
  try {
    // 如果有 tokenValue，先尝试使用 getLoginUserVO 接口获取用户信息
    if (userLoginCache && userStore.profile?.tokenValue) {
      console.log('尝试使用现有 token 获取用户信息')
      const res: any = await getLoginUserVoUsingGet({ options: {} })

      if (res.code === 200 && res.data) {
        // 保存用户信息到 store
        userStore.setProfile(res.data)
        console.log('使用现有 token 获取用户信息成功')
        return true
      }

      // 如果获取失败，继续走登录流程
      console.log('现有 token 无效，需要重新登录')
    }

    // 使用微信登录获取 code
    return new Promise((resolve) => {
      uni.login({
        provider: 'weixin',
        success: (loginRes) => {
          if (loginRes.errMsg === 'login:ok') {
            handleLogin(loginRes.code).then((success) => {
              resolve(success)
            })
          } else {
            userStore.setLoginState({ isLoggingIn: false, loginError: '登录失败' })
            resolve(false)
          }
        },
        fail: () => {
          userStore.setLoginState({ isLoggingIn: false, loginError: '登录失败' })
          resolve(false)
        },
      })
    })
  } catch (err) {
    console.error('登录过程出错:', err)
    userStore.setLoginState({ isLoggingIn: false, loginError: '登录失败' })
    return false
  }
}

/**
 * 处理登录
 * @param code
 */
export const handleLogin = async (code: string) => {
  try {
    const res: any = await wxLoginUsingPost({
      body: { code: code },
      options: {},
    })
    console.log('登录结果:', res)
    if (res.code === 200) {
      // 保存用户信息到 store
      userStore.setProfile(res.data)
      return true
    } else {
      userStore.setLoginState({ isLoggingIn: false, loginError: '登录失败' })
      return false
    }
  } catch (err) {
    console.error('登录失败:', err)
    uni.showToast({
      title: '服务器繁忙!',
      icon: 'error',
    })
    userStore.setLoginState({ isLoggingIn: false, loginError: '登录失败' })
    return false
  }
}

// 导出 waitForLogin 函数
export const waitForLogin = () => {
  return userStore.waitForLogin()
}
