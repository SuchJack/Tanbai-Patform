<template>
  <view class="popup-mask" v-if="show" @click="closePopup">
    <view class="popup-content" @click.stop>
      <view class="popup-header">
        <text class="title">授权头像和昵称</text>
      </view>

      <view class="popup-body">
        <!-- 头像选择区域 -->
        <view class="form-item">
          <text class="label">头像</text>
          <button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
            <view class="avatar-container">
              <image class="avatar" :src="tempAvatar" mode="aspectFill" />
              <text class="change-text">点击更换</text>
            </view>
          </button>
        </view>

        <!-- 昵称输入区域 -->
        <form @submit="handleSubmit">
          <view class="form-item">
            <text class="label">昵称</text>
            <input
              type="nickname"
              class="nickname-input"
              :value="tempNickname"
              placeholder="请输入昵称"
              name="nickname"
            />
          </view>
          <view class="popup-footer">
            <button class="btn cancel-btn" @click="closePopup">
              <text class="iconfont icon-close"></text>
              取消
            </button>
            <button class="btn confirm-btn" form-type="submit">
              <text class="iconfont icon-check"></text>
              确定
            </button>
          </view>
        </form>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useUserStore } from '../stores'
import { uploadFileAPI } from '../services/files'
import { COS_HOST } from '../constant'

const props = defineProps<{
  show: boolean
}>()

const emit = defineEmits(['update:show', 'confirm'])

const userStore = useUserStore()
const tempAvatar = ref(userStore.profile?.avatarUrl || '')
const tempNickname = ref(userStore.profile?.nickName || '')

// 监听用户信息变化
watch(
  () => userStore.profile,
  (newProfile) => {
    if (newProfile) {
      tempAvatar.value = newProfile.avatarUrl
      tempNickname.value = newProfile.nickName
    }
  },
  { deep: true },
)

// 选择头像
const onChooseAvatar = async (e: any) => {
  const { avatarUrl } = e.detail
  try {
    // 上传文件到服务器
    const uploadResult = await uploadFileAPI({ avatarUrl, userStore })

    // 打印上传结果
    console.log('上传结果:', uploadResult)

    // 解析响应数据
    let result
    try {
      result = JSON.parse(uploadResult.data)
      // console.log('解析后的响应数据:', result)
    } catch (parseErr) {
      // console.error('响应数据解析失败:', uploadResult.data)
      uni.showToast({
        title: '上传失败!',
        icon: 'error',
      })
    }

    // 检查响应状态
    if (uploadResult.statusCode !== 200) {
      uni.showToast({
        title: '服务器繁忙!',
        icon: 'error',
      })
    }
    console.log('rescode' + result.code)
    // 检查业务状态码
    if (result.code != 200) {
      uni.showToast({
        title: '上传失败!图片非法或图片过大',
        icon: 'none',
      })
    }

    if (result.code == 200) {
      // 保存服务器返回的永久图片地址
      tempAvatar.value = COS_HOST + result.data
      // console.log('设置的临时头像地址:', tempAvatar.value)

      // 显示成功提示
      uni.showToast({
        title: '头像上传成功,请确认修改!',
        icon: 'none',
      })
    }
  } catch (err) {
    console.error('头像上传失败:', err)
    uni.showToast({
      title: err.message || '头像上传失败',
      icon: 'error',
    })
  }
}

// 表单提交
const handleSubmit = (e: any) => {
  const nickname = e.detail.value.nickname.trim()
  if (!nickname || !tempAvatar.value) {
    uni.showToast({
      title: '请设置头像和昵称',
      icon: 'none',
    })
    return
  }

  emit('confirm', {
    avatarUrl: tempAvatar.value,
    nickName: nickname,
  })
  closePopup()
}

// 关闭弹窗
const closePopup = () => {
  emit('update:show', false)
}
</script>

<style lang="scss">
.popup-mask {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(8rpx);
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;

  .popup-content {
    width: 650rpx;
    background: #fff;
    border-radius: 24rpx;
    overflow: hidden;
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.12);
    animation: slideUp 0.3s ease;

    .popup-header {
      padding: 40rpx;
      text-align: center;
      border-bottom: 1rpx solid rgba(0, 0, 0, 0.06);

      .title {
        font-size: 36rpx;
        font-weight: 600;
        color: #333;
        letter-spacing: 1rpx;
      }
    }

    .popup-body {
      padding: 40rpx;

      .form-item {
        display: flex;
        align-items: center;
        margin-bottom: 40rpx;

        &:last-child {
          margin-bottom: 0;
        }

        .label {
          width: 120rpx;
          font-size: 32rpx;
          color: #333;
          font-weight: 500;
        }

        .avatar-wrapper {
          flex: 1;
          display: flex;
          justify-content: center;
        }

        .avatar-btn {
          padding: 0;
          width: 140rpx;
          height: 140rpx;
          background: none;
          border: none;
          transition: all 0.3s ease;

          &:active {
            transform: scale(0.95);
          }

          &::after {
            border: none;
          }

          .avatar-container {
            width: 100%;
            height: 100%;
            position: relative;

            .avatar {
              width: 100%;
              height: 100%;
              border-radius: 50%;
              object-fit: cover;
              border: 4rpx solid #fff;
              box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
              transition: all 0.3s ease;
            }

            .change-text {
              position: absolute;
              left: 50%;
              top: 60%;
              transform: translateX(-50%);
              font-size: 24rpx;
              color: #1e1f22;
              padding: 2rpx 6rpx;
              border-radius: 4rpx;
              white-space: nowrap;
              z-index: 1;
              letter-spacing: 1rpx;
              opacity: 0.8;
            }
          }
        }

        .nickname-input {
          flex: 1;
          height: 80rpx;
          background: #f5f7fa;
          border-radius: 12rpx;
          padding: 0 24rpx;
          font-size: 30rpx;
          color: #333;
          text-align: center;
          transition: all 0.3s ease;

          &:focus {
            background: #eef1f6;
            box-shadow: inset 0 0 0 2rpx #4080ff;
          }

          &::placeholder {
            color: #999;
            text-align: center;
          }
        }
      }
    }

    .popup-footer {
      display: flex;
      padding: 20rpx;
      border-top: 1rpx solid rgba(0, 0, 0, 0.06);
      gap: 20rpx;

      .btn {
        flex: 1;
        height: 88rpx;
        font-size: 32rpx;
        border-radius: 44rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;

        .iconfont {
          font-size: 36rpx;
          margin-right: 8rpx;

          &.icon-check {
            font-size: 32rpx;
          }
        }

        &::after {
          border: none;
        }

        &.cancel-btn {
          background: #f5f7fa;
          color: #666;

          &:active {
            background: #eef1f6;
          }
        }

        &.confirm-btn {
          background: #4080ff;
          color: #fff;

          &:active {
            background: #3674e6;
          }
        }
      }
    }
  }
}

@keyframes slideUp {
  from {
    transform: translateY(40rpx);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
