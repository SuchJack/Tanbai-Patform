<template>
  <view class="share-container">
    <!-- 分享图片区域 -->
    <view class="share-card" @click="previewImage">
      <template v-if="loading">
        <view class="loading-wrapper">
          <view class="loading-icon"></view>
          <text class="loading-text">海报生成中...</text>
        </view>
      </template>
      <image
        v-else
        :src="shareImageUrl"
        mode="widthFix"
        class="share-image"
        @load="onImageLoad"
        @error="onImageError"
      />
    </view>

    <!-- 提示文字 -->
    <view class="tip-text">
      点击图片可预览，如生成失败或图片失效；
      <text class="refresh-link" @click="refreshPoster">点击重新获取</text>
      ，如无法保存，截图保存即可。
      <text>更换头像后需要重新获取，"帮Ta分享"不能更换头像</text>
    </view>

    <!-- 底部按钮组  TODO 优化保存 -->
    <view class="bottom-buttons">
      <button class="save-btn" @click="handleSaveImage">
        <text class="iconfont icon-save"></text>
        保存图片
      </button>
      <button class="detail-btn" @click="goToDetail">
        <text class="iconfont icon-detail"></text>
        查看详情
      </button>
      <button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
        <text class="iconfont icon-avatar"></text>
        更换头像
      </button>
    </view>

    <!-- 保存提示文字 -->
    <view class="save-tip"> 若无法"保存图片"，点击预览长按保存即可</view>

    <!-- 隐藏的画布 -->
    <canvas
      canvas-id="shareCanvas"
      class="share-canvas"
      style="width: 600px; height: 600px; position: fixed; left: -9999px"
    />
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useUserStore } from '@/stores'
import { COS_HOST, DEFAULT_AVATAR } from '@/constant'
import { updateUserProfileAPI } from '@/services/user'
import { getPostBase64, uploadFileAPI } from '@/services/files'
import { handleCodeAndLogin } from '@/composables'

const userStore = useUserStore()
const shareImageUrl = ref('') // 新增：分享图片URL
const userAvatar = ref(userStore.profile?.avatarUrl || DEFAULT_AVATAR)

// 添加 loading 状态
const loading = ref(true)

const getPoster = (questionId: any) => {
  console.log('当前页面id:', questionId)

  // 先获取问题详情
  if (questionId) {
    createPoster(questionId)
  } else {
    uni.showToast({
      title: '服务繁忙',
      icon: 'error',
    })
  }
}

const createPoster = async (questionId: any) => {
  loading.value = true
  try {
    // 使用 getPostBase64 API 直接获取 base64 图片数据
    const res: any = await getPostBase64({ questionId })
    console.log('获取海报：', res.data)
    if (res.code === 200 && res.data) {
      // 直接使用 base64 数据作为图片源
      shareImageUrl.value = res.data
      console.log('获取海报成功')
    } else {
      throw new Error('获取海报失败')
    }
  } catch (err) {
    console.error('生成海报失败:', err)
    uni.showToast({
      title: '生成海报失败',
      icon: 'error',
    })
  } finally {
    loading.value = false
  }
}

// 保存分享图片
const handleSaveImage = async () => {
  try {
    // 第一步：立即获取相册权限
    const authRes = await new Promise((resolve) => {
      uni.getSetting({
        success: (res) => {
          if (!res.authSetting['scope.writePhotosAlbum']) {
            uni.authorize({
              scope: 'scope.writePhotosAlbum',
              success: () => resolve(true),
              fail: () => {
                uni.showModal({
                  title: '提示',
                  content: '需要您授权保存图片到相册',
                  success: (modalRes) => {
                    if (modalRes.confirm) {
                      uni.openSetting()
                    }
                  },
                })
                resolve(false)
              },
            })
          } else {
            resolve(true)
          }
        },
        fail: () => resolve(false),
      })
    })

    if (!authRes) return

    // 第二步：获取问题ID
    const pages: any = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    const questionId = currentPage.options?.id || currentPage.$page?.options?.id

    if (!questionId) {
      uni.showToast({
        title: '获取问题ID失败',
        icon: 'error',
      })
      return
    }

    uni.showLoading({ title: '正在保存...' })

    // 如果当前没有图片数据，重新获取
    if (!shareImageUrl.value) {
      const res: any = await getPostBase64({ questionId })
      if (res.code === 200 && res.data) {
        shareImageUrl.value = res.data
      } else {
        throw new Error('获取海报失败')
      }
    }

    // 将 base64 图片保存到相册
    // 首先将 base64 转为本地临时文件
    const base64Data = shareImageUrl.value.split(',')[1]
    const filePath = `${wx.env.USER_DATA_PATH}/poster_${Date.now()}.png`

    await new Promise((resolve, reject) => {
      wx.getFileSystemManager().writeFile({
        filePath,
        data: wx.base64ToArrayBuffer(base64Data),
        encoding: 'binary',
        success: () => resolve(true),
        fail: (err) => {
          console.error('写入文件失败:', err)
          reject(new Error('写入文件失败'))
        }
      })
    })

    // 保存临时文件到相册
    await new Promise((resolve, reject) => {
      uni.saveImageToPhotosAlbum({
        filePath,
        success: () => resolve(true),
        fail: (err) => {
          console.error('保存到相册失败:', err)
          reject(new Error('保存到相册失败'))
        }
      })
    })

    uni.showToast({
      title: '保存成功',
      icon: 'success',
    })
  } catch (error: any) {
    console.error('保存图片失败:', error)
    uni.showToast({
      title: error?.message || '保存失败',
      icon: 'error',
    })
  } finally {
    uni.hideLoading()
  }
}

// 跳转到详情页
const goToDetail = () => {
  const pages: any = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const questionId = currentPage.options?.id || currentPage.$page?.options?.id

  if (!questionId) {
    uni.showToast({
      title: '获取问题ID失败',
      icon: 'error',
    })
    return
  }

  uni.navigateTo({
    url: `/pages/question/detail?id=${questionId}`,
  })
}

// 更换头像
const onChooseAvatar = async (e: any) => {
  const tempAvatar = e.detail.avatarUrl

  try {
    handleCodeAndLogin(true)
    // 上传头像
    const uploadRes = await uploadFileAPI({ avatarUrl: tempAvatar, userStore })

    if (uploadRes.statusCode === 200) {
      const res = JSON.parse(uploadRes.data)
      userAvatar.value = COS_HOST + res.data
      console.log('上传成功:', res.data)
      // 构建更新数据
      const updateData = {
        userId: userStore.profile?.userId,
        nickName: '',
        avatarUrl: userAvatar.value,
      }

      // 更新用户信息
      const newUserProfile: any = await updateUserProfileAPI(updateData)

      if (newUserProfile.code === 200) {
        userStore.setProfile({
          nickName: '',
          openId: '',
          userId: String(newUserProfile.data.userId),
          avatarUrl: newUserProfile.data.avatarUrl,
          tokenValue: newUserProfile.data.token,
        })
      }

      uni.showToast({
        title: '更新成功,点击重新获取!',
        icon: 'none',
      })
    }
  } catch (err) {
    console.error('更新头像失败:', err)
    uni.showToast({
      title: '更新失败',
      icon: 'error',
    })
  }
}

// 预览图片
const previewImage = () => {
  uni.previewImage({
    urls: [shareImageUrl.value],
    current: shareImageUrl.value,
  })
}

// 图片加载完成
const onImageLoad = () => {
  loading.value = false
}

// 图片加载失败
const onImageError = () => {
  loading.value = false
  uni.showToast({
    title: '图片加载失败',
    icon: 'error',
  })
}

// 重新获取海报
const refreshPoster = () => {
  const pages: any = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const questionId = currentPage.options?.id || currentPage.$page?.options?.id

  if (questionId) {
    createPoster(questionId)
  } else {
    uni.showToast({
      title: '获取问题ID失败',
      icon: 'error',
    })
  }
}

onMounted(() => {
  // 获取问题ID
  const pages: any = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const questionId = currentPage.options?.id || currentPage.$page?.options?.id
  getPoster(questionId)
})
</script>

<style lang="scss">
.share-container {
  padding: 30rpx;
  min-height: 100vh;
  background: #e6f1fd;

  .share-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
    overflow: hidden;

    .share-image {
      width: 100%;
      border-radius: 12rpx;
    }

    .loading-wrapper {
      height: 400rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: #f8f9fa;
      border-radius: 12rpx;

      .loading-icon {
        width: 60rpx;
        height: 60rpx;
        border: 4rpx solid #e5e5e5;
        border-top-color: #4facfe;
        border-radius: 50%;
        animation: spin 1s infinite linear;
      }

      .loading-text {
        margin-top: 20rpx;
        font-size: 28rpx;
        color: #999;
      }
    }
  }

  .tip-text {
    font-size: 28rpx;
    color: #666;
    text-align: center;
    padding: 20rpx;
    margin-bottom: 30rpx;
    line-height: 1.6;

    .refresh-link {
      color: #4facfe;
      display: inline-block;
      padding: 0 4rpx;

      &:active {
        opacity: 0.8;
      }
    }
  }

  .bottom-buttons {
    display: flex;
    justify-content: space-between;
    gap: 20rpx;
    margin-bottom: 20rpx;

    button {
      flex: 1;
      height: 88rpx;
      border-radius: 44rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28rpx;

      .iconfont {
        margin-right: 8rpx;
        font-size: 32rpx;
      }

      &.save-btn {
        background: linear-gradient(45deg, #4facfe, #00f2fe);
        color: #fff;
      }

      &.detail-btn,
      &.avatar-btn {
        background: #f8f9fa;
        color: #333;
      }
    }
  }

  .save-tip {
    text-align: center;
    color: #ff0000;
    font-size: 26rpx;
    padding: 10rpx 0;
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
