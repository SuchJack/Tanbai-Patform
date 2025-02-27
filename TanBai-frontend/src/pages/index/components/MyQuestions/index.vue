<template>
  <view class="my-questions" :class="{ 'dark-mode': false }">
    <view class="header">
      <text class="title">我的问题</text>
      <text class="subtitle">查看我创建的问题</text>
    </view>

    <scroll-view
      scroll-y
      class="question-list"
      @refresherrefresh="onRefresh"
      :refresher-enabled="true"
      :refresher-triggered="refreshing"
    >
      <view v-if="loading" class="loading">
        <uni-load-more status="loading" />
      </view>

      <view v-else-if="questions.length === 0" class="empty">
        <text class="iconfont icon-empty"></text>
        <text>还没有发起过问题</text>
      </view>

      <view
        v-else
        v-for="item in questions"
        :key="item.question.id"
        class="question-item"
        @click="viewQuestionDetail(item.question.id)"
      >
        <view class="question-content">
          <text class="content">{{ item.question.content }}</text>
          <text class="time">{{ formatDate(item.question.createTime) }}</text>
        </view>
        <view class="question-stats">
          <view class="stat-item">
            <text class="iconfont icon-answer"></text>
            <text>{{ item.answerCount || 0 }}个回答</text>
          </view>
          <view class="action-buttons">
            <button class="delete-btn" @click.stop="confirmDelete(item.question.id)">
              <text class="iconfont icon-delete"></text>
            </button>
            <text class="iconfont icon-arrow-right"></text>
          </view>
        </view>
      </view>
    </scroll-view>

    <view class="create-btn" @click="goToCreate">
      <text class="iconfont icon-plus"></text>
      创建新问题
    </view>
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useUserStore } from '@/stores'
import { formatDate } from '@/utils/format'
import { deleteQuestionAPI, getMyQuestionAPI } from '../../../../services/question'
import { handleCodeAndLogin } from '../../../../composables'

// 定义接口
interface Question {
  answerCount: number
  question: {
    id: number
    content: string
    creatorId: number
    createTime: string
    updateTime: string
  }
}

const userStore = useUserStore()
const questions = ref<Question[]>([])
const loading = ref(false)
const refreshing = ref(false)

const goToCreate = () => {
  uni.navigateTo({
    url: '/pages/index/components/CreateQuestion/index',
  })
}

// 获取我的问题列表
const getMyQuestions = async () => {
  loading.value = true
  try {
    const res = await getMyQuestionAPI({ userStore })

    if (res.data.code === 200) {
      questions.value = res.data.data
    } else {
      uni.showToast({
        title: '获取数据失败',
        icon: 'error',
      })
    }
  } catch (err) {
    console.error('获取问题列表失败:', err)
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
const onRefresh = () => {
  refreshing.value = true
  getMyQuestions()
}

// 查看问题详情
const viewQuestionDetail = (questionId: number) => {
  uni.navigateTo({
    url: `/pages/question/detail?id=${questionId}`,
  })
}

// 确认删除
const confirmDelete = (questionId: number) => {
  uni.showModal({
    title: '确认删除',
    content: '确定要删除这个问题吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const res = await deleteQuestionAPI({ questionId, userStore })

          if (res.data.code === 200) {
            uni.showToast({
              title: '删除成功',
              icon: 'success',
            })
            // 重新获取问题列表
            getMyQuestions()
          } else if (res.statusCode === 401) {
            handleCodeAndLogin(false)
          } else {
            uni.showToast({
              title: '删除失败',
              icon: 'error',
            })
          }
        } catch (err) {
          console.error('删除失败:', err)
          uni.showToast({
            title: '删除失败',
            icon: 'error',
          })
        }
      }
    },
  })
}

// 获取 code 登录凭证
onMounted(() => {
  handleCodeAndLogin(true)
})
onMounted(async () => {
  // 等待0.5秒
  await new Promise((resolve) => setTimeout(resolve, 500))
  getMyQuestions()
})
</script>

<style lang="scss">
.my-questions {
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
      background: linear-gradient(45deg, #4facfe, #00f2fe);
      -webkit-background-clip: text;
      color: transparent;
    }

    .subtitle {
      font-size: 28rpx;
      color: #666;
    }
  }

  .question-list {
    height: calc(100vh - 300rpx);
    padding: 0 30rpx;

    .question-item {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

      .question-content {
        margin-bottom: 20rpx;

        .content {
          font-size: 32rpx;
          color: #333;
          margin-bottom: 10rpx;
          display: block;
        }

        .time {
          font-size: 24rpx;
          color: #999;
        }
      }

      .question-stats {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .stat-item {
          display: flex;
          align-items: center;
          font-size: 24rpx;
          color: #666;

          .iconfont {
            margin-right: 10rpx;
            font-size: 28rpx;
          }
        }

        .icon-arrow-right {
          font-size: 32rpx;
          color: #999;
        }
      }

      .action-buttons {
        display: flex;
        align-items: center;
        gap: 20rpx;

        .delete-btn {
          background: none;
          border: none;
          padding: 10rpx;
          margin: 0;
          line-height: 1;

          .icon-delete {
            font-size: 36rpx;
            color: #ff6b6b;
          }

          &:active {
            opacity: 0.8;
          }
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

  .create-btn {
    position: fixed;
    bottom: 40rpx;
    left: 30rpx;
    right: 30rpx;
    height: 88rpx;
    background: linear-gradient(45deg, #4facfe, #00f2fe);
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
</style>
