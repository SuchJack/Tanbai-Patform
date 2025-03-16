import { defineStore } from 'pinia'
import { ref } from 'vue'

// 用户信息类型
export interface User {
  userId: string
  nickName: string
  openId: string
  tokenValue: string
  avatarUrl: string
}

// 定义 Store
export const useUserStore = defineStore(
  'user',
  () => {
    // 用户信息
    const profile = ref<User>()

    // 登录状态
    const loginState = ref({
      isLoggingIn: false, // 是否正在登录中
      isLoggedIn: false, // 是否已登录
      loginError: null, // 登录错误信息
    })

    // 登录队列，存储等待登录完成后需要执行的回调函数
    const loginCallbackQueue: any = []

    // 保存用户信息
    const setProfile = (val: User) => {
      profile.value = val
      // 设置登录状态为已登录
      setLoginState({ isLoggedIn: true, isLoggingIn: false, loginError: null })
    }

    // 清理用户信息
    const clearProfile = () => {
      profile.value = undefined
      // 设置登录状态为未登录
      setLoginState({ isLoggedIn: false, isLoggingIn: false })
    }

    // 设置登录状态
    const setLoginState = (state: any) => {
      loginState.value = { ...loginState.value, ...state }

      // 如果登录成功，执行所有等待的回调
      if (state.isLoggedIn && !state.isLoggingIn) {
        executeCallbacks()
      }
    }

    // 添加登录完成后的回调
    const addLoginCallback = (callback: any) => {
      return new Promise((resolve) => {
        // 如果已经登录，直接执行回调
        if (loginState.value.isLoggedIn && !loginState.value.isLoggingIn) {
          const result = callback()
          resolve(result)
        } else {
          // 否则，将回调添加到队列
          loginCallbackQueue.push(() => {
            const result = callback()
            resolve(result)
          })
        }
      })
    }

    // 执行所有等待的回调
    const executeCallbacks = () => {
      while (loginCallbackQueue.length > 0) {
        const callback = loginCallbackQueue.shift()
        callback()
      }
    }

    // 等待登录完成
    const waitForLogin = () => {
      return new Promise((resolve: any) => {
        if (loginState.value.isLoggedIn && !loginState.value.isLoggingIn) {
          resolve()
        } else {
          addLoginCallback(() => resolve())
        }
      })
    }

    // 返回 state actions
    return {
      profile,
      loginState,
      setProfile,
      clearProfile,
      setLoginState,
      waitForLogin,
      addLoginCallback,
    }
  },
  {
    persist: {
      storage: {
        getItem(key) {
          return uni.getStorageSync(key)
        },
        setItem(key, value) {
          uni.setStorageSync(key, value)
        },
      },
    },
  },
)
