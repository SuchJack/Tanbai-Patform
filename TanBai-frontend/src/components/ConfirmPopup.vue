<template>
  <uni-popup ref="popup" type="center" :maskClick="false">
    <view class="confirm-popup">
      <view class="popup-content">
        <view class="title">温馨提示</view>
        <view class="message">{{ message }}</view>
        <view class="buttons">
          <button class="btn cancel-btn" @click="handleCancel">返回主页</button>
          <button class="btn confirm-btn" @click="handleConfirm">进入问答</button>
        </view>
      </view>
    </view>
  </uni-popup>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  show: boolean
  message: string
}>()

const emit = defineEmits<{
  (e: 'update:show', value: boolean): void
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

const popup = ref<any>(null)

// 监听show属性变化
watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      popup.value?.open()
    } else {
      popup.value?.close()
    }
  },
)

const handleConfirm = () => {
  emit('update:show', false)
  emit('confirm')
}

const handleCancel = () => {
  emit('update:show', false)
  emit('cancel')
}
</script>

<style lang="scss">
.confirm-popup {
  width: 580rpx;
  background: #fff;
  border-radius: 24rpx;
  overflow: hidden;

  .popup-content {
    padding: 40rpx;

    .title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      text-align: center;
      margin-bottom: 30rpx;
    }

    .message {
      font-size: 28rpx;
      color: #666;
      text-align: center;
      margin-bottom: 40rpx;
      padding: 0 20rpx;
    }

    .buttons {
      display: flex;
      gap: 20rpx;

      .btn {
        flex: 1;
        height: 80rpx;
        border-radius: 40rpx;
        font-size: 28rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border: none;

        &.cancel-btn {
          background: #f5f5f5;
          color: #666;
        }

        &.confirm-btn {
          background: linear-gradient(45deg, #4facfe, #00f2fe);
          color: #fff;
        }

        &:active {
          opacity: 0.8;
        }
      }
    }
  }
}
</style>
