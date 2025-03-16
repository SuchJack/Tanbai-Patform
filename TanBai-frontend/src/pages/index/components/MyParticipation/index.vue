<template>
  <view class="my-participation" :class="{ 'dark-mode': false }">
    <view class="header">
      <text class="title">我参与的</text>
      <text class="subtitle">我回答过的问题</text>
    </view>

    <scroll-view
      scroll-y
      class="answer-list"
      @refresherrefresh="onRefresh"
      :refresher-enabled="true"
      :refresher-triggered="refreshing"
    >
      <view v-if="loading" class="loading">
        <uni-load-more status="loading" />
      </view>

      <!-- 登录错误时显示重试按钮 -->
      <view v-else-if="loginError" class="login-error">
        <text>登录状态异常，请下拉刷新重试</text>
      </view>

      <view v-else-if="answers.length === 0" class="empty">
        <text class="iconfont icon-empty"></text>
        <text>还没有参与回答问题</text>
      </view>

      <view
        v-else
        v-for="answer in answers"
        :key="answer.answer.id"
        class="answer-item"
        @click="viewQuestionDetail(answer.answer.questionId)"
      >
        <view class="question-info">
          <view class="creator-info">
            <image
              class="avatar"
              :src="answer.questionCreatorAvatarUrl || '/static/default-avatar.png'"
              mode="aspectFill"
            />
            <text class="nickname">{{ answer.questionCreatorNickName }}</text>
            <text class="time">{{ formatDate(answer.questionCreateTime) }}</text>
          </view>
          <text class="question-content">{{ answer.questionContent }}</text>
        </view>

        <view class="answer-content">
          <text class="label">我的回答：</text>
          <text class="content">{{ answer.answer.content }}</text>
          <text class="time">{{ formatDate(answer.answer.createTime) }}</text>
        </view>

        <text class="iconfont icon-arrow-right"></text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Answer } from '@/types/answer'

import { useUserStore } from '@/stores'
import { formatDate } from '@/utils/format'
import { getMyParticipateQuestionsAPI } from '@/services/question'
import { handleCodeAndLogin, waitForLogin } from '@/composables'
import { onShow } from '@dcloudio/uni-app'

const userStore = useUserStore()
const answers = ref<Answer[]>([])
const loading = ref(true)
const refreshing = ref(false)
const loginError = ref(false)

// 获取我参与的回答
const fetchMyAnswers = async () => {
  loading.value = true
  try {
    // 等待登录完成
    await waitForLogin()
    const userId = userStore.profile?.userId
    const res: any = await getMyParticipateQuestionsAPI({ userId })
    if (res.code === 200) {
      answers.value = res.data || []
      loginError.value = false
    } else if (res.code === 401 || res.code === 40100) {
      // 登录失败或未登录
      loginError.value = true
    }
  } catch (err) {
    console.error('获取我的回答失败:', err)
    uni.showToast({
      title: '获取数据失败',
      icon: 'error',
    })
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 下拉刷新
const onRefresh = async () => {
  refreshing.value = true
  await handleCodeAndLogin(true)
  await fetchMyAnswers()
}

// 查看问题详情
const viewQuestionDetail = (questionId: number) => {
  uni.navigateTo({
    url: `/pages/question/detail?id=${questionId}`,
  })
}

// 页面显示时检查登录状态
onShow(() => {
  handleCodeAndLogin(true)
  fetchMyAnswers()
})
</script>

<style lang="scss">
.my-participation {
  min-height: 100vh;
  background: #e6f1fd;
  transition: all 0.3s ease;

  .header {
    padding: 30rpx;

    .title {
      font-size: 48rpx;
      font-weight: bold;
      margin-bottom: 10rpx;
      display: block;
      background: linear-gradient(45deg, #43e97b, #38f9d7);
      -webkit-background-clip: text;
      color: transparent;
    }

    .subtitle {
      font-size: 28rpx;
      color: #666;
    }
  }

  .answer-list {
    height: calc(100vh - 200rpx);
    padding: 0 30rpx;

    .answer-item {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

      .question-info {
        margin-bottom: 20rpx;

        .creator-info {
          display: flex;
          align-items: center;
          margin-bottom: 12rpx;

          .avatar {
            width: 40rpx;
            height: 40rpx;
            border-radius: 50%;
            margin-right: 12rpx;
          }

          .nickname {
            font-size: 26rpx;
            color: #666;
            margin-right: 12rpx;
          }

          .time {
            font-size: 24rpx;
            color: #999;
          }
        }

        .question-content {
          font-size: 32rpx;
          color: #333;
          font-weight: 500;
        }
      }

      .answer-content {
        .label {
          font-size: 26rpx;
          color: #666;
          margin-bottom: 8rpx;
          display: block;
        }

        .content {
          font-size: 28rpx;
          color: #333;
          margin-bottom: 8rpx;
          display: block;
        }

        .time {
          font-size: 24rpx;
          color: #999;
          display: block;
        }
      }
    }

    .empty {
      text-align: center;
      padding: 100rpx 0;
      color: #999;

      .iconfont {
        font-size: 96rpx;
        margin-bottom: 20rpx;
        display: block;
      }
    }
  }

  /* 添加登录错误样式 */
  .login-error {
    text-align: center;
    padding: 100rpx 0;
    color: #ff6b6b;
    font-size: 28rpx;
  }
}
</style>
