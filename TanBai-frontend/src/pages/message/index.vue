<template>
  <view class="service-container">
    <view class="service-content">
      <!--      <view class="message-settings">-->
      <!--        <view class="setting-item">-->
      <!--          <text class="label">留言通知</text>-->
      <!--          <switch :checked="isSubscribed" @change="handleSubscribeChange" />-->
      <!--        </view>-->
      <!--        <view class="setting-desc">开启后将收到新的留言通知</view>-->
      <!--      </view>-->

      <view class="divider"></view>

      <view class="service-info">
        <view class="section-title">联系客服</view>
        <view class="info-item">
          <text class="label">客服微信：</text>
          <text class="value" selectable>{{ customerService.wechat ?? 'sunfar25' }}</text>
        </view>

        <view class="info-item">
          <text class="label">客服邮箱：</text>
          <text class="value" selectable>{{ customerService.email ?? '2640808535@qq.com' }}</text>
        </view>

        <view class="info-item">
          <text class="label">工作时间：</text>
          <text class="value">{{ customerService.workingHours ?? '周一至周五 9:00-21:00' }}</text>
        </view>
      </view>

      <view class="tips">温馨提示：请在工作时间内联系客服，我们会尽快回复您的问题。</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { requestSubscribeMessage } from '../../services/message'
import { getCustomerServiceInfo } from '../../services/customerService'

const customerService = ref({
  id: 0,
  wechat: '',
  email: '',
  workingHours: '',
})

onMounted(async () => {
  try {
    const res = await getCustomerServiceInfo()
    customerService.value = res
  } catch (err) {
    // console.error('获取客服信息失败:', err)
    // uni.showToast({
    //   title: '获取客服信息失败',
    //   icon: 'error'
    // })
  }
})

// const isSubscribed = ref(false)
</script>

<style lang="scss">
.service-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 30rpx;

  .service-content {
    background: #fff;
    border-radius: 20rpx;
    padding: 40rpx;

    .message-settings {
      margin-bottom: 30rpx;

      .setting-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10rpx;

        .label {
          font-size: 28rpx;
          color: #333;
        }
      }

      .setting-desc {
        font-size: 24rpx;
        color: #999;
      }
    }

    .divider {
      height: 1rpx;
      background: #eee;
      margin: 30rpx 0;
    }

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 30rpx;
    }

    .service-info {
      margin-bottom: 40rpx;

      .info-item {
        margin-bottom: 30rpx;

        .label {
          font-size: 28rpx;
          color: #666;
          margin-right: 20rpx;
        }

        .value {
          font-size: 28rpx;
          color: #333;
        }
      }
    }

    .tips {
      font-size: 26rpx;
      color: #999;
      background: #f8f9fa;
      padding: 20rpx;
      border-radius: 10rpx;
    }
  }
}
</style>
