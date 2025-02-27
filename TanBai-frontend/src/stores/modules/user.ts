import { defineStore } from 'pinia'
import { ref } from 'vue'

// 用户信息类型
export interface User {
  id: number
  nickName: string
  openId: string
  token: string
  avatarUrl: string
}

// 定义 Store
export const useUserStore = defineStore(
  'user',
  () => {
    // 用户信息
    const profile = ref<User>()

    // 保存用户信息
    const setProfile = (val: User) => {
      profile.value = val
    }

    // 清理用户信息
    const clearProfile = () => {
      profile.value = undefined
    }
    // const updateProfile = (profile) => {
    //   profile.value = profile
    // }

    // 返回 state actions
    return {
      profile,
      setProfile,
      clearProfile,
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
