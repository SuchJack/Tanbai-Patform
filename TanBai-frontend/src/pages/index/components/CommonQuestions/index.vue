<template>
  <view class="common-questions" :class="{ 'dark-mode': false }">
    <view class="header">
      <text class="title">常见问题</text>
      <text class="subtitle">坦白驿站小程序Q&A</text>
    </view>

    <!--    <view class="search-box">-->
    <!--      <text class="iconfont icon-search"></text>-->
    <!--      <input-->
    <!--        v-model="searchKeyword"-->
    <!--        type="text"-->
    <!--        placeholder="搜索问题..."-->
    <!--        @confirm="searchQuestions"-->
    <!--      />-->
    <!--    </view>-->

    <scroll-view scroll-y class="qa-list">
      <view v-if="loading" class="loading">
        <uni-load-more status="loading" />
      </view>

      <view v-else-if="qaList.length === 0" class="empty">
        <text class="iconfont icon-empty"></text>
        <text>没有找到相关问题</text>
      </view>

      <view v-else v-for="qa in qaList" :key="qa.id" class="qa-item">
        <view class="question">
          <text class="q-icon">Q</text>
          <text class="q-text">{{ qa.question }}</text>
        </view>
        <view class="answer">
          <text class="a-icon">A</text>
          <text class="a-text">{{ qa.answer }}</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { SystemQAndA } from '@/types/qa'
import { getCommonQuestionAPI } from '../../../../services/common'

const qaList = ref<SystemQAndA[]>([])
// const searchKeyword = ref('')
const loading = ref(false)

// 获取常见问题列表
const getCommonQuestions = async () => {
  loading.value = true
  try {
    const res = await getCommonQuestionAPI()
    if (res.statusCode === 200) {
      qaList.value = res.data.data
    }
  } catch (err) {
    console.error('获取常见问题失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getCommonQuestions()
})
</script>

<style lang="scss">
.common-questions {
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
      background: linear-gradient(45deg, #fa709a, #fee140);
      -webkit-background-clip: text;
      color: transparent;
    }

    .subtitle {
      font-size: 28rpx;
      color: #666;
    }
  }

  .search-box {
    margin: 0 30rpx 30rpx;
    background: #ffffff;
    border-radius: 40rpx;
    padding: 20rpx 30rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    .icon-search {
      font-size: 36rpx;
      color: #999;
      margin-right: 20rpx;
    }

    input {
      flex: 1;
      font-size: 28rpx;
    }
  }

  .qa-list {
    height: calc(100vh - 300rpx);
    padding: 0 30rpx;

    .qa-item {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

      .question {
        display: flex;
        align-items: flex-start;
        margin-bottom: 20rpx;

        .q-icon {
          width: 40rpx;
          height: 40rpx;
          background: #ff6b6b;
          color: #ffffff;
          border-radius: 20rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24rpx;
          margin-right: 20rpx;
          flex-shrink: 0;
        }

        .q-text {
          font-size: 32rpx;
          color: #333;
          flex: 1;
        }
      }

      .answer {
        display: flex;
        align-items: flex-start;

        .a-icon {
          width: 40rpx;
          height: 40rpx;
          background: #4facfe;
          color: #ffffff;
          border-radius: 20rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24rpx;
          margin-right: 20rpx;
          flex-shrink: 0;
        }

        .a-text {
          font-size: 28rpx;
          color: #666;
          flex: 1;
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
}
</style>
