<script setup lang="ts">
import { ref } from 'vue'

// 获取系统信息
const { safeAreaInsets, theme = 'light' } = uni.getSystemInfoSync()

// 导航按钮配置
const navButtons = [
  {
    title: '创建问题',
    icon: 'plus-circle',
    desc: '有问必答,看看Ta想对你说什么',
    gradient: 'linear-gradient(45deg, #FF6B6B, #FF8E53)',
    path: '/pages/index/components/CreateQuestion/index',
  },
  {
    title: '我的问题',
    icon: 'question-circle',
    desc: '查看我发起的问题',
    gradient: 'linear-gradient(45deg, #4facfe, #00f2fe)',
    path: '/pages/index/components/MyQuestions/index',
  },
  {
    title: '我参与的',
    icon: 'comments',
    desc: '我参与讨论的问题',
    gradient: 'linear-gradient(45deg, #43e97b, #38f9d7)',
    path: '/pages/index/components/MyParticipation/index',
  },
  {
    title: '常见问题',
    icon: 'book',
    desc: '查看热门问答',
    gradient: 'linear-gradient(45deg, #fa709a, #fee140)',
    path: '/pages/index/components/CommonQuestions/index',
  },
]

const isDarkMode = ref(theme === 'dark')

// 页面跳转方法
const handleNavigate = (path: string) => {
  uni.navigateTo({
    url: path,
    fail: (err) => {
      console.error('页面跳转失败:', err)
    },
  })
}
</script>

<template>
  <view
    class="navbar"
    :class="{ 'dark-mode': false }"
    :style="{ paddingTop: safeAreaInsets?.top + 10 + 'px' }"
  >
    <!-- Logo区域 -->
    <view class="logo-section">
      <image class="logo-image" src="@/static/images/logo.jpg" mode="aspectFit"></image>
      <text class="logo-text">坦白话 · 真心话 · 对你说</text>
    </view>

    <!-- 导航按钮区域 -->
    <view class="nav-buttons">
      <view
        v-for="(btn, index) in navButtons"
        :key="index"
        class="nav-button-wrapper"
        :style="{ animationDelay: `${index * 0.1}s` }"
        @click="handleNavigate(btn.path)"
      >
        <view class="nav-button" :style="{ background: btn.gradient }">
          <view class="button-content">
            <view class="icon-wrapper">
              <text :class="['iconfont', `icon-${btn.icon}`]"></text>
            </view>
            <view class="text-content">
              <text class="title">{{ btn.title }}</text>
              <text v-if="btn.desc" class="description">{{ btn.desc }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss">
.navbar {
  background-image: url(@/static/images/navigator_bg.png);
  background-size: cover;
  min-height: 100vh;
  transition: all 0.3s ease;
}

.logo-section {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;

  .logo-image {
    width: 166rpx;
    height: 39rpx;
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }
  }

  .logo-text {
    margin-left: 20rpx;
    padding-left: 20rpx;
    border-left: 2rpx solid rgba(255, 255, 255, 0.8);
    font-size: 26rpx;
    color: #ffffff;
    font-weight: 300;
  }
}

.nav-buttons {
  padding: 30rpx;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300rpx, 1fr));
  gap: 20rpx;

  .nav-button-wrapper {
    opacity: 0;
    animation: fadeInUp 0.5s ease forwards;
  }

  .nav-button {
    border-radius: 20rpx;
    padding: 30rpx;
    height: 100%;
    transition: all 0.3s ease;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-6rpx);
      box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
    }

    .button-content {
      display: flex;
      align-items: center;
      gap: 20rpx;

      .icon-wrapper {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;

        .iconfont {
          font-size: 40rpx;
          color: #ffffff;
        }
      }

      .text-content {
        flex: 1;

        .title {
          font-size: 32rpx;
          font-weight: 600;
          color: #ffffff;
          margin-bottom: 8rpx;
          display: block;
        }

        .description {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.8);
          display: block;
        }
      }
    }
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .nav-buttons {
    grid-template-columns: 1fr;
  }
}
</style>
