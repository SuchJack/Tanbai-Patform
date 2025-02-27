<template>
  <view class="create-question" :class="{ 'dark-mode': false }">
    <view class="header">
      <text class="title">创建问题</text>
      <text class="subtitle">提出你想问的问题</text>
    </view>

    <view class="main-content">
      <textarea
        v-model="content"
        class="question-input"
        placeholder="在这里输入你的问题..."
        :maxlength="200"
        auto-height
      />

      <view class="reference-section">
        <text class="section-title">参考问题</text>
        <view class="reference-list">
          <view
            v-for="(item, index) in referenceQuestions"
            :key="index"
            class="reference-item"
            @click="content = item.content"
          >
            <text class="reference-text">{{ item.content }}</text>
            <text class="reference-category">{{ item.category }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="footer">
      <button class="submit-btn" :loading="loading" @click="createQuestion">
        <text class="iconfont icon-send"></text>
        发布问题
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { QuestionReference } from '@/types/question'
import { useUserStore } from '../../../../stores'
import { createQuestionAPI, getReferencesAPI } from '../../../../services/question'
import { handleCodeAndLogin } from '../../../../composables'
import * as timers from 'node:timers'

const userStore = useUserStore()
const content = ref('')
const loading = ref(false)
const referenceQuestions = ref<QuestionReference[]>([])

// 获取参考问题
const getReferences = async () => {
  try {
    const res = await getReferencesAPI({ userStore })
    if (res.statusCode === 200) {
      referenceQuestions.value = res.data.data
    }
  } catch (err) {
    console.error('获取参考问题失败:', err)
    uni.showToast({
      title: '获取失败',
      icon: 'none',
    })
  }
}

// 创建问题
const createQuestion = async () => {
  if (!content.value.trim()) {
    uni.showToast({
      title: '请输入问题内容',
      icon: 'none',
    })
    return
  }

  if (!userStore.profile?.id) {
    uni.showToast({
      title: '请求错误,请重新进入小程序',
      icon: 'none',
    })
    return
  }

  // 检查模板消息订阅权限
  try {
    const subscribeResult = await uni.requestSubscribeMessage({
      tmplIds: [
        'r10K00ltI42x0K3dkCW46tpmYgmLeipRr-La835wnJo',
        's-ge-X2pNg0FDJTICfr-AeHLFUpbzxYSy-aNzg7998o',
      ],
    })

    if (subscribeResult.errMsg === 'requestSubscribeMessage:ok') {
      // 用户同意了订阅或已经订阅过，继续创建问题
      await handleCreateQuestion()
    } else {
      // 用户拒绝了订阅，显示提示后继续创建
      uni.showModal({
        title: '订阅提醒',
        content: '为了及时接收回复通知，建议您开启订阅消息',
        confirmText: '确定',
        success: async (res) => {
          if (res.confirm) {
            // 用户点击确定，继续创建问题
            await handleCreateQuestion()
          }
        },
      })
    }
  } catch (err) {
    console.error('请求订阅消息失败:', err)
    // 发生错误时仍然允许创建问题
    await handleCreateQuestion()
  }
}

// 处理创建问题的具体逻辑
const handleCreateQuestion = async () => {
  loading.value = true

  try {
    const res = await createQuestionAPI({
      userStore: userStore,
      content: content.value,
    })

    if (res.data.code === 200) {
      uni.showToast({
        title: '创建成功',
        icon: 'success',
      })

      console.log('res' + res)
      // 获取创建的问题ID
      const questionId = res.data.data.id

      // 延迟跳转，等待 Toast 显示完
      setTimeout(() => {
        // 跳转到问题详情页
        uni.redirectTo({
          url: `/pages/question/detail?id=${questionId}`,
        })
      }, 500)

      content.value = ''
    }
    if (res.data.code === 500) {
      uni.showToast({
        title: '内容非法,请修改',
        icon: 'none',
      })
    }
  } catch (err) {
    console.error('创建失败:', err)
    uni.showToast({
      title: '创建失败',
      icon: 'error',
    })
  } finally {
    loading.value = false
  }
}

// 获取 code 登录凭证
onMounted(() => {
  handleCodeAndLogin(true)
})

onMounted(async () => {
  // 等待0.5秒
  await new Promise((resolve) => setTimeout(resolve, 500))
  getReferences()
})
</script>

<style lang="scss">
.create-question {
  min-height: 100vh;
  padding: 30rpx;
  background: #e6f1fd;
  transition: all 0.3s ease;

  .header {
    margin-bottom: 40rpx;

    .title {
      font-size: 48rpx;
      font-weight: bold;
      margin-bottom: 10rpx;
      display: block;
      background: linear-gradient(45deg, #ff6b6b, #ff8e53);
      -webkit-background-clip: text;
      color: transparent;
    }

    .subtitle {
      font-size: 28rpx;
      color: #666;
    }
  }

  .main-content {
    .question-input {
      width: 90%;
      min-height: 200rpx;
      padding: 30rpx;
      background: #ffffff;
      border-radius: 20rpx;
      margin-bottom: 40rpx;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
      font-size: 32rpx;
    }

    .reference-section {
      .section-title {
        font-size: 32rpx;
        font-weight: bold;
        margin-bottom: 20rpx;
        display: block;
      }

      .reference-list {
        .reference-item {
          background: #ffffff;
          padding: 20rpx;
          border-radius: 16rpx;
          margin-bottom: 20rpx;
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
          transition: all 0.3s ease;

          &:active {
            transform: scale(0.98);
          }

          .reference-text {
            font-size: 28rpx;
            color: #333;
            display: block;
          }

          .reference-category {
            font-size: 24rpx;
            color: #999;
            margin-top: 10rpx;
            display: block;
          }
        }
      }
    }
  }

  .footer {
    position: fixed;
    bottom: 40rpx;
    left: 30rpx;
    right: 30rpx;

    .submit-btn {
      width: 100%;
      height: 88rpx;
      background: linear-gradient(45deg, #ff6b6b, #ff8e53);
      border-radius: 44rpx;
      color: #ffffff;
      font-size: 32rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .iconfont {
        margin-right: 10rpx;
        font-size: 36rpx;
      }

      &:active {
        opacity: 0.9;
      }
    }
  }
}
</style>
