<template>
  <view class="my-container" :class="{ 'dark-mode': false }">
    <!-- 顶部用户信息卡片 -->
    <view class="user-card">
      <!-- 头像显示区域 -->
      <view class="info-item">
        <text class="label">头像：</text>
        <view class="avatar-wrapper">
          <image
            class="avatar"
            :src="userStore.profile?.avatarUrl || DEFAULT_AVATAR"
            mode="aspectFill"
          />
        </view>
      </view>

      <!-- 昵称显示区域 -->
      <view class="info-item">
        <text class="label">昵称：</text>
        <view class="nickname-wrapper">
          <text class="nickname-text">{{ userStore.profile?.nickName || '未设置昵称' }}</text>
        </view>
      </view>

      <!-- 修改个人信息链接 -->
      <view class="profile-edit">
        <text class="edit-link" @click="showPopup = true">
          <text class="iconfont icon-edit"></text>
          修改个人信息
        </text>
      </view>
    </view>

    <!-- 温馨提示 -->
    <view class="tip-card">
      <view class="tip-content">
        <text class="iconfont icon-info"></text>
        <text class="tip-text">温馨提示：新用户建议设置头像和昵称。</text>
      </view>
    </view>

    <!-- 底部链接 -->
    <view class="bottom-links">
      <navigator class="link-item" url="/pages/agreement/index">
        <text class="iconfont icon-agreement"></text>
        <text class="text">用户协议</text>
        <text class="iconfont icon-arrow-right"></text>
      </navigator>

      <navigator class="link-item" url="/pages/message/index">
        <text class="iconfont icon-service"></text>
        <text class="text">联系客服</text>
        <text class="iconfont icon-arrow-right"></text>
      </navigator>

      <!--      <navigator class="link-item" url="/pages/message/index">-->
      <!--        <text class="iconfont icon-message"></text>-->
      <!--        <text class="text">消息设置</text>-->
      <!--        <text class="iconfont icon-arrow-right"></text>-->
      <!--      </navigator>-->
    </view>

    <!-- 用户信息修改弹窗 -->
    <UserInfoPopup v-model:show="showPopup" @confirm="handleUserInfoUpdate" />
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useUserStore } from '../../stores'
import { DEFAULT_AVATAR } from '../../constant'
import { handleCodeAndLogin } from '../../composables'
import { updateUserProfileAPI } from '../../services/user'
import UserInfoPopup from '../../components/UserInfoPopup.vue'

const userStore = useUserStore()
const showPopup = ref(false)
const loading = ref(false)

// 获取 code 登录凭证
onMounted(() => handleCodeAndLogin(false))

// 检测头像和昵称并提示用户
onMounted(async () => {
  // 等待2秒
  await new Promise((resolve) => setTimeout(resolve, 2000))
  console.log('检测头像和昵称')
  if (
    userStore.profile?.nickName == '未设置昵称' ||
    userStore.profile?.avatarUrl == '' ||
    userStore.profile?.nickName == ''
  ) {
    uni.showModal({
      title: '提示',
      content: '新用户需要设置头像和昵称',
      showCancel: false,
      success: () => {
        showPopup.value = true
      },
    })
  }
})

// 处理用户信息更新
const handleUserInfoUpdate = async (info: { avatarUrl: string; nickName: string }) => {
  if (!userStore.profile) {
    await handleCodeAndLogin(false)
    if (!userStore.profile) {
      uni.showToast({
        title: '获取用户信息失败',
        icon: 'error',
      })
      return
    }
  }

  loading.value = true

  try {
    // 构建更新数据
    const updateData = {
      userId: userStore.profile.id,
      nickName: info.nickName,
      avatarUrl: info.avatarUrl,
    }

    // 先发送到后端
    const res = await updateUserProfileAPI(updateData)

    // 检查响应状态码
    if (res.statusCode !== 200) {
      throw new Error('更新失败')
    }

    // 解析响应数据
    let responseData
    try {
      responseData = typeof res.data === 'string' ? JSON.parse(res.data) : res.data
      console.log('更新响应数据:', responseData)
    } catch (parseError) {
      console.error('解析响应数据失败:', parseError)
      throw new Error('数据解析失败')
    }

    // 检查业务状态码
    if (responseData.code !== 200) {
      throw new Error(responseData.message || '更新失败')
    }

    // 确保后端更新成功后，再更新本地数据
    if (responseData.data) {
      userStore.setProfile({
        ...userStore.profile!,
        avatarUrl: info.avatarUrl,
        nickName: info.nickName,
      })
    } else {
      userStore.setProfile({
        ...userStore.profile!,
        avatarUrl: info.avatarUrl,
        nickName: info.nickName,
      })
    }

    uni.showToast({
      title: '修改成功',
      icon: 'success',
    })

    // 关闭弹窗
    showPopup.value = false
  } catch (err: any) {
    console.error('更新失败:', err)
    uni.showToast({
      title: err?.message || '更新失败',
      icon: 'error',
    })
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss">
.my-container {
  min-height: 100vh;
  background: #e6f1fd;
  padding: 30rpx;
  transition: all 0.3s ease;

  .user-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 50rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 40rpx;

      .label {
        width: 100rpx;
        font-size: 28rpx;
        color: #333;
      }

      .avatar-wrapper {
        flex: 1;
        display: flex;
        justify-content: center;
        padding-left: 30rpx;

        .avatar {
          width: 120rpx;
          height: 120rpx;
          border-radius: 50%;
          border: 8rpx solid #efeded;
          box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
        }
      }

      .nickname-wrapper {
        flex: 1;
        display: flex;
        justify-content: center;
        padding-left: 30rpx;

        .nickname-text {
          flex: 1;
          height: 80rpx;
          background: linear-gradient(to right, #efeded, #ece8e8);
          border-radius: 40rpx;
          padding: 0 30rpx;
          font-size: 32rpx;
          color: #333;
          text-align: center;
          line-height: 80rpx;
          box-shadow: inset 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
          position: relative;
          font-weight: 500;
          letter-spacing: 1px;

          &::after {
            content: '';
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            height: 2rpx;
            background: linear-gradient(
              to right,
              transparent,
              rgba(134, 188, 237, 0.3),
              transparent
            );
          }
        }
      }
    }

    .profile-edit {
      text-align: center;
      margin-top: 20rpx;
      padding: 20rpx 0;

      .edit-link {
        font-size: 28rpx;
        color: #4080ff;
        font-weight: 500;
        display: flex;
        align-items: center;
        justify-content: center;

        .iconfont {
          margin-right: 10rpx;
          font-size: 32rpx;
        }

        &:active {
          opacity: 0.8;
        }
      }
    }
  }

  .bottom-links {
    background: #fff;
    border-radius: 20rpx;
    overflow: hidden;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

    .link-item {
      height: 100rpx;
      display: flex;
      align-items: center;
      padding: 0 30rpx;
      font-size: 28rpx;
      color: #333;
      background: #fff;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        left: 30rpx;
        right: 30rpx;
        bottom: 0;
        height: 1px;
        background: #f2f2f2;
      }

      &:last-child::after {
        display: none;
      }

      .iconfont {
        font-size: 36rpx;
        margin-right: 20rpx;

        &.icon-arrow-right {
          margin-right: 0;
          margin-left: auto;
          color: #999;
        }
      }

      &.contact {
        margin: 0;
        border: none;
        border-radius: 0;

        &::after {
          border: none;
        }
      }

      &:active {
        background: #f8f9fa;
      }
    }
  }

  .tip-card {
    background: #fff7e6;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 30rpx;
    display: flex;
    justify-content: center;
    align-items: center;

    .tip-content {
      display: flex;
      align-items: center;
      justify-content: center;

      .iconfont {
        font-size: 36rpx;
        color: #ffaa00;
        margin-right: 12rpx;
      }

      .tip-text {
        font-size: 26rpx;
        color: #ff9900;
        text-align: center;
      }
    }
  }
}

.menu-list {
  margin-top: 20rpx;
  background: #fff;
  border-radius: 16rpx;

  .menu-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .iconfont {
      font-size: 36rpx;
      color: #333;
      margin-right: 20rpx;

      &.icon-arrow-right {
        margin-left: auto;
        margin-right: 0;
        color: #999;
        font-size: 24rpx;
      }
    }

    .label {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.nickname-text {
  font-size: 32rpx;
  color: #333;
  padding: 16rpx;
}
</style>
