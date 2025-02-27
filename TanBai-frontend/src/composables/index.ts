import { getUserLoginAPI } from '../services/user'
import { useUserStore } from '../stores'

const userStore = useUserStore()

/**
 * 处理 code 和 登录
 */
export const handleCodeAndLogin = async (checkLoginStatus: any) => {
  // 如何openId不为空时，跳过执行逻辑
  // 检测的目的是为了减少数据库的访问，但是对于新用户来说，这个不能跳过
  if (checkLoginStatus) {
    if (userStore.profile?.openId != null) {
      console.log('openId is not null')
      return true
    }
  }
  uni.login({
    provider: 'weixin',
    success: (loginRes) => {
      if (loginRes.errMsg === 'login:ok') {
        handleLogin(loginRes.code)
        // console.log('code' + loginRes.code) // 调试
      }
    },
  })
}

/**
 * 处理登录
 * @param code
 */
export const handleLogin = async (code: string) => {
  try {
    const res = await getUserLoginAPI({ code })
    if (res.data.code === 200) {
      // 保存用户信息到 store
      userStore.setProfile(res.data.data)
    }
  } catch (err) {
    console.error('登录失败:', err)
    uni.showToast({
      title: '服务器繁忙!',
      icon: 'error',
    })
  }
}
