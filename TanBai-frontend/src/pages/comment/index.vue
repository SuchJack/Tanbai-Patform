<template>
  <view class="comment-page">
    <!-- 头部 -->
    <view class="header">
      <text class="title">写留言</text>
      <text class="close" @click="goBack">×</text>
    </view>

    <!-- 输入区域 -->
    <view class="input-section">
      <textarea
        v-model="commentContent"
        class="comment-input"
        placeholder="写下你的留言..."
        :maxlength="200"
        auto-height
      />
      <text class="word-count">{{ commentContent.length }}/200</text>
    </view>

    <!-- 参考示例 -->
    <view class="reference-section" v-if="references.length > 0">
      <view class="section-title">参考示例（点击选择）</view>
      <view class="reference-list">
        <view
          v-for="(item, index) in references"
          :key="index"
          class="reference-item"
          @click="useReference(item.content)"
        >
          {{ item.content }}
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <button
      class="submit-btn"
      :disabled="!commentContent.trim()"
      :loading="submitting"
      @click="submitComment"
    >
      发送
    </button>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores'
import { submitCommentAPI } from '@/services/question'
import { getRandomReferencesAPI } from '../../services/answer'

const userStore = useUserStore()
const commentContent = ref('')
const submitting = ref(false)
const references = ref<string[]>([])
const questionId = ref<number>()

// 获取随机参考回答
const getRandomReferences = async () => {
  try {
    const res = await getRandomReferencesAPI(5)
    if (res.statusCode === 200 && res.data.code === 200) {
      references.value = res.data.data
    }
  } catch (err) {
    console.error('获取参考回答失败:', err)
  }
}

// 使用参考回答
const useReference = (content: string) => {
  commentContent.value = content
}

// 提交留言
const submitComment = async () => {
  if (!commentContent.value.trim()) return

  submitting.value = true
  try {
    const res = await submitCommentAPI({
      questionId: questionId.value,
      content: commentContent.value,
      userStore: userStore,
    })

    if (res.data.code === 500) {
      uni.showToast({
        title: '内容非法,请修改',
        icon: 'none',
      })
    }

    if (res.statusCode === 200 && res.data.code === 200) {
      uni.showToast({
        title: '留言成功',
        icon: 'success',
      })
      // 使用redirectTo直接跳转到详情页，替换当前页面
      setTimeout(() => {
        uni.redirectTo({
          url: `/pages/question/detail?id=${questionId.value}`,
        })
      }, 10)
    }
  } catch (err) {
    console.error('留言失败:', err)
    uni.showToast({
      title: '留言失败',
      icon: 'error',
    })
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

onMounted(() => {
  // 获取问题ID
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  questionId.value = Number(currentPage.options.questionId)

  // 获取随机参考
  getRandomReferences()
})
</script>

<style lang="scss">
.comment-page {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 30rpx;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;

    .title {
      font-size: 36rpx;
      font-weight: bold;
    }

    .close {
      font-size: 48rpx;
      color: #999;
      padding: 10rpx;
    }
  }

  .input-section {
    background: #fff;
    border-radius: 20rpx;
    padding: 20rpx;
    margin-bottom: 30rpx;
    position: relative;

    .comment-input {
      width: 100%;
      min-height: 200rpx;
      font-size: 28rpx;
      line-height: 1.6;
    }

    .word-count {
      position: absolute;
      bottom: 20rpx;
      right: 20rpx;
      font-size: 24rpx;
      color: #999;
    }
  }

  .reference-section {
    margin-bottom: 30rpx;

    .section-title {
      font-size: 28rpx;
      color: #666;
      margin-bottom: 20rpx;
    }

    .reference-list {
      .reference-item {
        background: #fff;
        border-radius: 16rpx;
        padding: 20rpx;
        margin-bottom: 20rpx;
        font-size: 28rpx;
        color: #333;
        line-height: 1.6;

        &:active {
          opacity: 0.8;
        }
      }
    }
  }

  .submit-btn {
    width: 100%;
    height: 88rpx;
    border-radius: 44rpx;
    background: linear-gradient(45deg, #4facfe, #00f2fe);
    color: #fff;
    font-size: 32rpx;
    margin-top: 40rpx;

    &:disabled {
      opacity: 0.5;
    }
  }
}
</style>
