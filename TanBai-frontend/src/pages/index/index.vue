<script setup lang="ts">
import UserInfoPopup from '@/components/UserInfoPopup.vue'
import { onMounted, ref } from 'vue'
import { handleCodeAndLogin } from '../../composables'
import { useUserStore } from '../../stores'
import { updateUserProfileAPI } from '../../services/user'
// 获取屏幕边界到安全区域距离
const { safeAreaInsets } = uni.getSystemInfoSync()
const userStore = useUserStore()
const showUserInfoPopup = ref(false)

// 获取 code 登录凭证
onMounted(() => {
  handleCodeAndLogin(false)
})

// 检测头像和昵称并提示用户
onMounted(async () => {
  // 等待2秒确保登录完成
  await new Promise((resolve) => setTimeout(resolve, 2000))
  if (
    userStore.profile?.nickName == '未设置昵称' ||
    userStore.profile?.nickName == '' ||
    !userStore.profile?.avatarUrl
  ) {
    // showUserInfoPopup.value = true // 取消自动弹出
  }
})

// 处理用户信息更新
const handleUserInfoConfirm = async (userInfo: { avatarUrl: string; nickName: string }) => {
  try {
    // 更新用户信息
    const res = await updateUserProfileAPI({
      userId: userStore.profile?.id,
      ...userInfo,
    })

    if (res.data.code === 500) {
      uni.showToast({
        title: '内容非法,请修改',
        icon: 'none',
      })
    }
    if (res.data.code === 200) {
      // 更新本地存储的用户信息
      userStore.setProfile({
        ...userStore.profile!,
        avatarUrl: userInfo.avatarUrl,
        nickName: userInfo.nickName,
      })

      uni.showToast({
        title: '设置成功',
        icon: 'success',
      })
    }
  } catch (err) {
    console.error('更新用户信息失败:', err)
    uni.showToast({
      title: '设置失败',
      icon: 'error',
    })
  }
}

// 页面跳转
const navigateTo = (url: string) => {
  uni.navigateTo({ url })
}

// 定义分享给好友的回调函数
const onShareAppMessage = (res: any) => {
  return {
    title: '坦白驿站 - 好友间的秘密空间',
    path: '/pages/index/index',
    imageUrl: 'https://tbpic.moshanghong.site/user_avatar/default_avatar/share-cover.jpg', // 请确保此图片存在
  }
}

// 定义分享到朋友圈的回调函数
// const onShareTimeline = () => {
//   return {
//     title: '坦白驿站 - 好友间的秘密空间',
//     query: '',
//     imageUrl: '/static/share-cover.png', // 请确保此图片存在
//   }
// }
</script>

<template>
  <view class="index">
    <!-- 页面标题 -->
    <view class="page-title">
      <text class="title-text">好友间的</text>
      <text class="title-text">坦白驿站</text>
    </view>

    <!-- 创建问题按钮 -->
    <view class="create-section">
      <view class="create-card" @click="navigateTo('/pages/index/components/CreateQuestion/index')">
        <view class="content">
          <text class="title">有问必答</text>
          <text class="subtitle">看Ta想对你说些什么</text>
        </view>
        <button class="create-btn">开始创建</button>
      </view>
    </view>

    <!-- 功能按钮组 -->
    <view class="feature-section">
      <view class="feature-card" @click="navigateTo('/pages/index/components/MyQuestions/index')">
        <text class="iconfont icon-wenti"></text>
        <text class="title">我的问题</text>
        <text class="subtitle">我发起的问题</text>
      </view>

      <view
        class="feature-card"
        @click="navigateTo('/pages/index/components/MyParticipation/index')"
      >
        <text class="iconfont icon-canyu"></text>
        <text class="title">我参与的</text>
        <text class="subtitle">我回答的问题</text>
      </view>

      <view
        class="feature-card"
        @click="navigateTo('/pages/index/components/CommonQuestions/index')"
      >
        <text class="iconfont icon-help"></text>
        <text class="title">常见问题</text>
        <text class="subtitle">坦白驿站Q&A</text>
      </view>
    </view>

    <!-- 个人信息修改链接 -->
    <view class="profile-edit">
      <text class="edit-link" @click="showUserInfoPopup = true">修改个人信息</text>
    </view>

    <!-- 底部协议 -->
    <view class="agreement">
      使用前请阅读
      <text class="agreement-link" @click="navigateTo('/pages/agreement/index')">用户协议</text>
      若不同意，请勿使用本应用。
    </view>
  </view>

  <!-- 用户信息设置弹窗 -->
  <UserInfoPopup v-model:show="showUserInfoPopup" @confirm="handleUserInfoConfirm" />
</template>

<style lang="scss">
.index {
  min-height: 100vh;
  padding: 30rpx;
  background: #e6f1fd;

  .page-title {
    margin: 30rpx 0 30rpx;
    text-align: left;
    padding: 0 20rpx;

    .title-text {
      font-size: 70rpx;
      font-weight: bold;
      color: #333;
      display: block;
      line-height: 1.4;
    }
  }

  .create-section {
    margin-bottom: 40rpx;
    padding: 0 20rpx;

    .create-card {
      background: #4080ff;
      border-radius: 20rpx;
      padding: 40rpx;
      position: relative;
      overflow: hidden;
      box-shadow: 0 8rpx 32rpx rgba(64, 128, 255, 0.2);
      display: flex;
      justify-content: space-between;
      align-items: center;

      .content {
        flex: 1;
        margin-right: 40rpx;
      }

      .title {
        font-size: 40rpx;
        color: #fff;
        font-weight: 600;
        margin-bottom: 8rpx;
        display: block;
      }

      .subtitle {
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.9);
        display: block;
      }

      .create-btn {
        background: #fff;
        min-width: 200rpx;
        height: 80rpx;
        border-radius: 40rpx;
        color: #4080ff;
        font-size: 30rpx;
        font-weight: 500;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
        flex-shrink: 0;

        &:active {
          transform: scale(0.98);
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
        }
      }

      // 添加背景装饰
      &::after {
        content: '';
        position: absolute;
        right: -40rpx;
        bottom: -40rpx;
        width: 200rpx;
        height: 200rpx;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 50%;
      }

      &::before {
        content: '';
        position: absolute;
        right: 40rpx;
        bottom: 40rpx;
        width: 100rpx;
        height: 100rpx;
        background: rgba(255, 255, 255, 0.15);
        border-radius: 50%;
      }
    }
  }

  .feature-section {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20rpx;
    padding: 0 20rpx;
    max-width: 1200rpx;
    margin: 0 auto;

    .feature-card {
      border-radius: 20rpx;
      padding: 30rpx 20rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:nth-child(1) {
        background: #7d9af3; // 浅紫色
        .iconfont {
          color: #fff;
          background: none;
          -webkit-background-clip: initial;
        }

        .title {
          color: #fff;
        }

        .subtitle {
          color: rgba(255, 255, 255, 0.8);
        }
      }

      &:nth-child(2) {
        background: #98c872; // 浅绿色
        .iconfont {
          color: #fff;
          background: none;
          -webkit-background-clip: initial;
        }

        .title {
          color: #fff;
        }

        .subtitle {
          color: rgba(255, 255, 255, 0.8);
        }
      }

      &:nth-child(3) {
        background: #6bc2f1; // 浅蓝色
        .iconfont {
          color: #fff;
          background: none;
          -webkit-background-clip: initial;
        }

        .title {
          color: #fff;
        }

        .subtitle {
          color: rgba(255, 255, 255, 0.8);
        }
      }

      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.03);
      }

      .iconfont {
        font-size: 48rpx;
        margin-bottom: 16rpx;
      }

      .title {
        font-size: 28rpx;
        font-weight: 500;
        margin-bottom: 8rpx;
      }

      .subtitle {
        font-size: 22rpx;
        text-align: center;
      }
    }
  }

  // 添加个人信息修改链接样式
  .profile-edit {
    text-align: center;
    margin-top: 20rpx;
    padding: 0 20rpx;

    .edit-link {
      font-size: 26rpx;
      color: #4080ff;
      font-weight: 500;
    }
  }

  // 添加底部协议样式
  .agreement {
    position: fixed;
    bottom: 40rpx;
    left: 0;
    right: 0;
    text-align: center;
    font-size: 25rpx;
    color: #999;
    padding: 0 40rpx;

    .agreement-link {
      color: #4080ff;
      display: inline;
    }
  }
}
</style>
