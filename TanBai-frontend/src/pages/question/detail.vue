<template>
  <view class="question-detail" :class="{ 'dark-mode': false }">
    <!-- 主要内容 -->
    <view v-if="loading" class="loading-container">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...请稍等</text>
      <text class="loading-text">首次加载需要时间，请耐心等待</text>
    </view>

    <view v-else>
      <!-- 问题内容卡片 -->
      <view class="question-section">
        <!-- 问题内容 -->
        <view class="question-content">
          {{ question.content ?? '网络繁忙或问题不存在,请稍后再试!' }}
        </view>

        <!-- 底部信息容器 -->
        <view class="question-footer">
          <!-- 问题时间 -->
          <text class="question-time">
            {{ formatDate(question.createTime) ?? '服务器繁忙,请稍后再试' }}
          </text>

          <!-- 提问者信息 -->
          <view class="creator-info">
            <image
              class="avatar"
              :src="question.creatorAvatarUrl || DEFAULT_AVATAR"
              mode="aspectFill"
            />
            <text class="nickname">{{ question.creatorNickName ?? 'null' }}</text>
          </view>
        </view>
      </view>

      <!-- 回答列表 -->
      <view class="answers-section">
        <view class="section-title">
          <text>Ta的留言</text>
          <text class="count">({{ answers.length }})</text>
          <text class="edit-profile" @click="updateUserInfo">修改头像昵称</text>
        </view>

        <view v-if="answers.length === 0" class="empty">
          <text class="iconfont icon-empty"></text>
          <text>暂无回答</text>
        </view>

        <view v-else class="answer-list">
          <view v-for="answer in answers" :key="answer.answer.id" class="answer-item">
            <view class="answer-header">
              <image
                class="avatar"
                :src="answer.userAvatarUrl || DEFAULT_AVATAR"
                mode="aspectFill"
              />
              <text class="nickname">{{ answer.userNickName }}</text>
              <text class="time">{{ formatDate(answer.answer.createTime) }}</text>
            </view>

            <view class="answer-content-wrapper">
              <text class="answer-content">{{ answer.answer.content }}</text>
              <!-- 回答的删除按钮：问题创建者或回答者本人可删除 -->
              <view
                v-if="
                  answer.answer.userId === userStore.profile?.id ||
                  question.creatorId === userStore.profile?.id
                "
                class="delete-btn"
                @click="confirmDeleteAnswer(answer.answer.id)"
              >
                <text class="iconfont icon-delete"></text>
              </view>
            </view>

            <!-- 回复列表 -->
            <view class="replies" v-if="answer.replies && answer.replies.length > 0">
              <!-- 回复列表内容 -->
              <view v-for="reply in answer.replies" :key="reply.id" class="reply-item">
                <!-- 当reply.content为"无权限查看"时显示查看按钮 -->
                <view v-if="reply.content === '无权限查看'" class="reply-header">
                  <view class="user-info">
                    <image
                      class="avatar"
                      :src="reply.userAvatarUrl || DEFAULT_AVATAR"
                      mode="aspectFill"
                    />
                    <button class="peek-btn reply-peek-btn" @click="showPayReplyAccessModal">
                      <text class="iconfont icon-eye"></text>
                      查看回复
                    </button>
                  </view>
                </view>

                <!-- 当有权限查看时显示正常内容 -->
                <view v-else class="reply-header">
                  <view class="user-info">
                    <image
                      class="avatar"
                      :src="reply.userAvatarUrl || DEFAULT_AVATAR"
                      mode="aspectFill"
                    />
                    <text class="reply-content">{{ reply.content }}</text>
                  </view>
                  <!-- 回复的删除按钮：问题创建者或回复者本人可删除 -->
                  <view
                    v-if="
                      reply.userId === userStore.profile?.id ||
                      question.creatorId === userStore.profile?.id
                    "
                    class="delete-btn"
                    @click="confirmDeleteReply(reply.id)"
                  >
                    <text class="iconfont icon-delete"></text>
                  </view>
                </view>
              </view>
            </view>
            <!-- 回复按钮 -->
            <view
              class="reply-btn"
              @click="showReplyModal(answer.answer.id)"
              v-if="userStore.profile?.id === question.creatorId"
            >
              <text class="iconfont icon-comment"></text>
              回复Ta
            </view>
          </view>
        </view>
      </view>

      <!-- 底部按钮 -->
      <view class="bottom-buttons">
        <!-- 创建者本人可见的按钮组 -->
        <template v-if="userStore.profile?.id === question.creatorId">
          <button class="share-btn-1" @click="goToShare">
            <text class="iconfont icon-share"></text>
            分享
          </button>
          <button class="peek-btn" @click="showPayModal">
            <text class="iconfont icon-eye"></text>
            偷看是谁
          </button>
          <button class="comment-btn" @click="showCommentModal">
            <text class="iconfont icon-comment"></text>
            立即留言
          </button>
        </template>

        <!-- 非创建者可见的按钮组 -->
        <template v-else>
          <button class="play-btn" @click="goToCreate">
            <text class="iconfont icon-plus"></text>
            我也要玩
          </button>
          <button class="share-btn-2" @click="goToShare">
            <text class="iconfont icon-share"></text>
            帮Ta分享
          </button>
          <button class="comment-btn" @click="showCommentModal">
            <text class="iconfont icon-comment"></text>
            立即留言
          </button>
        </template>
      </view>
    </view>

    <!-- 弹层组件放在最外层，确保不被loading遮挡 -->
    <!-- 用户信息设置弹窗 -->
    <UserInfoPopup v-model:show="showUserInfoPopup" @confirm="handleUserInfoConfirm" />

    <!-- 确认弹窗 -->
    <ConfirmPopup
      v-model:show="showConfirmPopup"
      message="您正在扫码进入好友问题，是否继续？"
      @confirm="handleScanConfirm"
      @cancel="handleScanCancel"
    />

    <!-- 回复弹窗 -->
    <uni-popup ref="replyPopup" type="center">
      <view class="reply-modal">
        <view class="modal-header">
          <text class="title">写回复</text>
          <text class="close" @click="closeReplyModal">×</text>
        </view>
        <textarea
          class="reply-input"
          v-model="replyContent"
          placeholder="请输入回复内容"
          :maxlength="200"
        />
        <button class="submit-btn" :disabled="!replyContent.trim()" @click="submitReply">
          发送
        </button>
      </view>
    </uni-popup>
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Question } from '@/types/question'
import type { Answer } from '@/types/answer'
import { useUserStore } from '@/stores'
import { formatDate } from '@/utils/format'
import {
  deleteCommentAPI,
  deleteReplyAPI,
  getQuestionDetailAPI,
  replyCommentAPI,
  submitCommentAPI,
} from '../../services/question'
import {
  createOrderAPI,
  getOrderStatusAPI,
  getReplyOrderStatusAPI,
  reqPayAPI,
  reqReplyPayAPI,
} from '../../services/orders'
import UserInfoPopup from '@/components/UserInfoPopup.vue'
import ConfirmPopup from '@/components/ConfirmPopup.vue'
import { updateUserProfileAPI } from '../../services/user'
import { handleCodeAndLogin } from '../../composables'
import { DEFAULT_AVATAR } from '../../constant'
import { onLoad } from '@dcloudio/uni-app'

const loading = ref(true)
const userStore = useUserStore()
const question = ref<Question>({} as Question)
const answers = ref<Answer[]>([])
const popup = ref()
const commentContent = ref('')
const submitting = ref(false)
const replyPopup = ref()
const replyContent = ref('')
const currentAnswerId = ref<number>()
const showUserInfoPopup = ref(false)
const scanQuestionId = ref()
const showConfirmPopup = ref(false)

// 点击弹出
const updateUserInfo = () => {
  showUserInfoPopup.value = true
  return
}

// 获取问题详情
const getQuestionDetail = async (questionId: number) => {
  loading.value = true
  try {
    const res = await getQuestionDetailAPI({ questionId, userStore })

    console.log('问题详情响应:', res)

    if (res.statusCode === 200 && res.data.code === 200) {
      question.value = res.data.data.question
      answers.value = res.data.data.answers || []
    } else if (res.statusCode === 401) {
      handleCodeAndLogin(false)
    }
  } catch (err) {
    console.error('获取问题详情失败:', err)
    uni.showToast({
      title: '请稍后再试！',
      icon: 'error',
    })
  } finally {
    loading.value = false
  }
}

// 处理扫码确认
const handleScanConfirm = async () => {
  try {
    await getQuestionDetail(Number(scanQuestionId.value))
  } catch (err) {
    console.error('获取问题详情失败:', err)
    uni.showToast({
      title: '加载失败，请稍后再试',
      icon: 'error',
    })
  }
}

// 处理扫码取消
const handleScanCancel = () => {
  loading.value = false
  uni.reLaunch({
    url: '/pages/index/index',
  })
}

// 添加 onLoad 生命周期函数
onLoad((query: any) => {
  // 处理扫码进入的场景
  if (query.scene) {
    // loading.value = true // 设置加载状态为true
    const questionId = decodeURIComponent(query.scene)
    handleCodeAndLogin(false)
    scanQuestionId.value = questionId
  }
})

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const questionId = currentPage.options.id
  if (scanQuestionId.value) {
    showConfirmPopup.value = true
  }
  if (!scanQuestionId.value && questionId) {
    getQuestionDetail(Number(questionId))
  }
})

// 跳转到创建问题页面
const goToCreate = () => {
  uni.navigateTo({
    url: '/pages/index/components/CreateQuestion/index',
  })
}

// 显示留言弹窗
const showCommentModal = async () => {
  handleCodeAndLogin(true)

  // 检查用户昵称
  if (
    userStore.profile?.nickName === '未设置昵称' ||
    userStore.profile?.nickName === '' ||
    !userStore.profile?.nickName
  ) {
    showUserInfoPopup.value = true
    return
  }

  // 检查模板消息订阅权限
  try {
    const subscribeResult = await uni.requestSubscribeMessage({
      tmplIds: [
        'r10K00ltI42x0K3dkCW46tpmYgmLeipRr-La835wnJo',
        's-ge-X2pNg0FDJTICfr-AeHLFUpbzxYSy-aNzg7998o',
      ], // 替换为您的模板消息ID
    })
    console.log('订阅结果:', subscribeResult)
    if (subscribeResult.errMsg === 'requestSubscribeMessage:ok') {
      // 用户同意了订阅或已经订阅过
      console.log('用户同意了订阅或已经订阅过')
      // 跳转到留言页面
      uni.navigateTo({
        url: `/pages/comment/index?questionId=${question.value.id}`,
      })
    } else {
      // 用户拒绝了订阅
      uni.showModal({
        title: '订阅提醒',
        content: '为了及时接收回复通知，建议您开启订阅消息',
        confirmText: '确定',
        success: (res) => {
          if (res.confirm) {
            // 用户点击确定，仍然跳转到留言页面
            uni.navigateTo({
              url: `/pages/comment/index?questionId=${question.value.id}`,
            })
          }
        },
      })
    }
  } catch (err) {
    console.error('请求订阅消息失败:', err)
    // 发生错误时仍然允许用户留言
    uni.navigateTo({
      url: `/pages/comment/index?questionId=${question.value.id}`,
    })
  }
}

// 关闭留言弹窗
const closeCommentModal = () => {
  popup.value.close()
  commentContent.value = ''
}

// 显示回复弹窗
const showReplyModal = async (answerId: number) => {
  handleCodeAndLogin(true)

  // 检查用户昵称
  if (
    userStore.profile?.nickName === '未设置昵称' ||
    userStore.profile?.nickName === '' ||
    !userStore.profile?.nickName
  ) {
    showUserInfoPopup.value = true
    return
  }

  // 检查模板消息订阅权限
  try {
    const subscribeResult = await uni.requestSubscribeMessage({
      tmplIds: [
        'r10K00ltI42x0K3dkCW46tpmYgmLeipRr-La835wnJo',
        's-ge-X2pNg0FDJTICfr-AeHLFUpbzxYSy-aNzg7998o',
      ], // 替换为您的模板消息ID
    })
    console.log('subscribeResult:', subscribeResult)
    if (subscribeResult.errMsg === 'requestSubscribeMessage:ok') {
      // 用户同意了订阅或已经订阅过
      console.log('用户同意了订阅或已经订阅过')
      currentAnswerId.value = answerId
      replyContent.value = ''
      replyPopup.value.open()
    } else {
      // 用户拒绝了订阅
      uni.showModal({
        title: '订阅提醒',
        content: '为了及时接收回复通知，建议您开启订阅消息',
        confirmText: '确定',
        success: (res) => {
          if (res.confirm) {
            // 用户点击确定，仍然打开回复弹窗
            currentAnswerId.value = answerId
            replyContent.value = ''
            replyPopup.value.open()
          }
        },
      })
    }
  } catch (err) {
    console.error('请求订阅消息失败:', err)
    // 发生错误时仍然允许用户回复
    currentAnswerId.value = answerId
    replyContent.value = ''
    replyPopup.value.open()
  }
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim() || submitting.value) return

  handleCodeAndLogin(true)
  submitting.value = true

  // 保存回复内容的副本
  const content = replyContent.value.trim()

  // 立即关闭弹窗
  closeReplyModal()

  try {
    const res = await replyCommentAPI({
      answerId: currentAnswerId.value,
      content: content, // 使用保存的内容副本
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
        title: '回复成功',
        icon: 'success',
      })
      // 刷新问题详情
      getQuestionDetail(question.value.id)
    } else if (res.statusCode === 401) {
      // token过期或无效，跳转登录
      handleCodeAndLogin(false)
    }
  } catch (err) {
    console.error('回复失败:', err)
    uni.showToast({
      title: '回复失败',
      icon: 'error',
    })
  } finally {
    submitting.value = false
  }
}

// 关闭回复弹窗
const closeReplyModal = () => {
  replyPopup.value.close()
  replyContent.value = '' // 关闭时清空内容
  submitting.value = false // 重置提交状态
}

// 确认删除回答
const confirmDeleteAnswer = (answerId: number) => {
  // 判断是否为问题创建者
  const isCreator = question.value.creatorId === userStore.profile?.id

  uni.showModal({
    title: '确认删除',
    content: isCreator ? '作为问题创建者，确定要删除这个回答吗？' : '确定要删除这个回答吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const res = await deleteCommentAPI({
            commentId: answerId,
            userStore,
            isCreator: isCreator,
          })

          if (res.data.code === 200) {
            uni.showToast({
              title: '删除成功',
              icon: 'success',
            })
            // 刷新问题详情
            getQuestionDetail(question.value.id)
          } else {
            throw new Error('删除失败')
          }
        } catch (err) {
          console.error('删除回答失败:', err)
          uni.showToast({
            title: '删除失败',
            icon: 'error',
          })
        }
      }
    },
  })
}

// 确认删除回复
const confirmDeleteReply = (replyId: number) => {
  // 判断是否为问题创建者
  const isCreator = question.value.creatorId === userStore.profile?.id

  uni.showModal({
    title: '确认删除',
    content: isCreator ? '作为问题创建者，确定要删除这条回复吗？' : '确定要删除这条回复吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const res = await deleteReplyAPI({ replyId, userStore, isCreator })

          if (res.data.code === 200) {
            uni.showToast({
              title: '删除成功',
              icon: 'success',
            })
            // 刷新问题详情
            getQuestionDetail(question.value.id)
          } else {
            throw new Error('删除失败')
          }
        } catch (err) {
          console.error('删除回复失败:', err)
          uni.showToast({
            title: '删除失败',
            icon: 'error',
          })
        }
      }
    },
  })
}

// 跳转到分享页面
const goToShare = () => {
  const questionId = question.value.id
  console.log('跳转分享页面，问题ID:', questionId) // 添加调试日志

  uni.navigateTo({
    url: `/pages/share/index?id=${questionId}`,
    success: (res) => {
      console.log('跳转成功:', res)
    },
    fail: (err) => {
      console.error('跳转失败:', err)
    },
  })
}

// 支付解锁头像
const showPayModal = () => {
  uni.showModal({
    title: '温馨提示',
    content: '支付5.20元解锁当前房间所有提问者头像和昵称',
    confirmText: '确认支付',
    cancelText: '取消',
    success: async (res) => {
      if (res.confirm) {
        try {
          // 1. 创建订单
          const createOrderRes = await createOrderAPI({ userStore })

          console.log('创建订单结果:', createOrderRes)

          if (createOrderRes.data.code === 200) {
            const orderInfo = createOrderRes.data.data

            // 2. 发起支付
            const payRes = await reqPayAPI({ orderNumber: orderInfo.number, userStore })

            console.log('发起支付结果:', payRes)

            if (payRes.data.code === 200) {
              // 3. 调用微信支付
              const payParams = payRes.data.data
              uni.requestPayment({
                provider: 'wxpay',
                orderInfo: '',
                timeStamp: payParams.timeStamp,
                nonceStr: payParams.nonceStr,
                package: payParams.packageStr,
                signType: payParams.signType,
                paySign: payParams.paySign,
                success: () => {
                  // 4. 支付成功后轮询订单状态
                  let pollCount = 0 // 轮询次数计数
                  const maxPollCount = 10 // 最大轮询次数

                  const checkPayStatus = async () => {
                    try {
                      // 超过最大轮询次数，显示支付失败
                      if (pollCount >= maxPollCount) {
                        uni.showToast({
                          title: '支付超时',
                          icon: 'error',
                        })
                        return
                      }

                      pollCount++ // 增加轮询次数
                      const statusRes = await getOrderStatusAPI({
                        orderNumber: orderInfo.number,
                        questionId: question.value.id,
                      })

                      console.log('支付状态查询结果:', statusRes)
                      if (statusRes.statusCode === 200) {
                        if (statusRes.data.data === 'SUCCESS') {
                          // 支付成功
                          uni.showToast({
                            title: '支付成功',
                            icon: 'success',
                          })
                          // 重新获取问题详情，刷新头像显示
                          getQuestionDetail(question.value.id)
                          return
                        } else if (statusRes.data.data === 'PENDING') {
                          // 继续轮询
                          setTimeout(checkPayStatus, 1000)
                        } else {
                          uni.showToast({
                            title: '支付失败',
                            icon: 'error',
                          })
                          return
                        }
                      }
                    } catch (err) {
                      console.error('查询支付状态失败:', err)
                      uni.showToast({
                        title: '查询支付状态失败',
                        icon: 'error',
                      })
                    }
                  }

                  // 开始轮询
                  checkPayStatus()
                },
                fail: (err) => {
                  console.error('支付失败:', err)
                  uni.showToast({
                    title: '支付失败',
                    icon: 'error',
                  })
                },
              })
            } else {
              throw new Error('发起支付失败')
            }
          } else {
            throw new Error('创建订单失败')
          }
        } catch (err) {
          console.error('支付失败:', err)
          uni.showToast({
            title: '支付失败',
            icon: 'error',
          })
        }
      }
    },
  })
}

// 支付解锁查看
const showPayReplyAccessModal = () => {
  uni.showModal({
    title: '温馨提示',
    content: '支付2.88元解锁当前房间的所有回答',
    confirmText: '确认支付',
    cancelText: '取消',
    success: async (res) => {
      if (res.confirm) {
        try {
          // 1. 创建订单
          const createOrderRes = await createOrderAPI({ userStore })

          console.log('创建订单结果:', createOrderRes)

          if (createOrderRes.data.code === 200) {
            const orderInfo = createOrderRes.data.data

            // 2. 发起支付
            const payRes = await reqReplyPayAPI({ orderNumber: orderInfo.number, userStore })

            console.log('发起支付结果:', payRes)

            if (payRes.data.code === 200) {
              // 3. 调用微信支付
              const payParams = payRes.data.data
              uni.requestPayment({
                provider: 'wxpay',
                orderInfo: '',
                timeStamp: payParams.timeStamp,
                nonceStr: payParams.nonceStr,
                package: payParams.packageStr,
                signType: payParams.signType,
                paySign: payParams.paySign,
                success: () => {
                  // 4. 支付成功后轮询订单状态
                  let pollCount = 0 // 轮询次数计数
                  const maxPollCount = 10 // 最大轮询次数

                  const checkPayStatus = async () => {
                    try {
                      // 超过最大轮询次数，显示支付失败
                      if (pollCount >= maxPollCount) {
                        uni.showToast({
                          title: '支付超时',
                          icon: 'error',
                        })
                        return
                      }

                      pollCount++ // 增加轮询次数
                      const statusRes = await getReplyOrderStatusAPI({
                        orderNumber: orderInfo.number,
                        questionId: question.value.id,
                      })

                      console.log('支付状态查询结果:', statusRes)
                      if (statusRes.statusCode === 200) {
                        if (statusRes.data.data === 'SUCCESS') {
                          // 支付成功
                          uni.showToast({
                            title: '支付成功',
                            icon: 'success',
                          })
                          // 重新获取问题详情，刷新头像显示
                          getQuestionDetail(question.value.id)
                          return
                        } else if (statusRes.data.data === 'PENDING') {
                          // 继续轮询
                          setTimeout(checkPayStatus, 1000)
                        } else {
                          uni.showToast({
                            title: '支付失败',
                            icon: 'error',
                          })
                          return
                        }
                      }
                    } catch (err) {
                      console.error('查询支付状态失败:', err)
                      uni.showToast({
                        title: '查询支付状态失败',
                        icon: 'error',
                      })
                    }
                  }

                  // 开始轮询
                  checkPayStatus()
                },
                fail: (err) => {
                  console.error('支付失败:', err)
                  uni.showToast({
                    title: '支付失败',
                    icon: 'error',
                  })
                },
              })
            } else {
              throw new Error('发起支付失败')
            }
          } else {
            throw new Error('创建订单失败')
          }
        } catch (err) {
          console.error('支付失败:', err)
          uni.showToast({
            title: '支付失败',
            icon: 'error',
          })
        }
      }
    },
  })
}

// 处理用户信息更新
const handleUserInfoConfirm = async (userInfo: { avatarUrl: string; nickName: string }) => {
  try {
    // 更新用户信息
    const res = await updateUserProfileAPI({
      userId: userStore.profile?.id,
      ...userInfo,
    })

    if (res.data.code === 500) {
      uni.showToast({
        title: '内容非法,请修改',
        icon: 'none',
      })
    }

    if (res.data.code === 200) {
      // 更新本地存储的用户信息
      userStore.setProfile({
        ...userStore.profile!,
        avatarUrl: userInfo.avatarUrl,
        nickName: userInfo.nickName,
      })

      uni.showToast({
        title: '设置成功',
        icon: 'success',
      })

      // 重新获取问题详情，刷新页面显示
      getQuestionDetail(question.value.id)
    }
  } catch (err) {
    console.error('更新用户信息失败:', err)
    uni.showToast({
      title: '设置失败',
      icon: 'error',
    })
  }
}

// 定义分享给好友的回调函数
const onShareAppMessage = (res: any) => {
  return {
    title: question.value.content || '来看看这个有趣的问题',
    path: `/pages/index/index`,
    imageUrl: 'https://tbpic.moshanghong.site/user_avatar/default_avatar/share-cover.jpg', // 请确保此图片存在
  }
}

// 定义分享到朋友圈的回调函数
// const onShareTimeline = () => {
//   return {
//     title: question.value.content || '来看看这个有趣的问题',
//     query: `id=${question.value.id}`,
//     imageUrl: 'https://tbpic.moshanghong.site/user_avatar/default_avatar/share-cover.jpg', // 请确保此图片存在
//   }
// }
</script>

<style lang="scss">
.question-detail {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 30rpx;
  padding-bottom: 140rpx;
  position: relative; // 添加相对定位

  .question-section {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    .question-content {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      line-height: 1.6;
      margin-bottom: 30rpx;
    }

    .question-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
      border-top: 1px solid #f0f0f0;

      .question-time {
        font-size: 24rpx;
        color: #999;
      }

      .creator-info {
        display: flex;
        align-items: center;

        .avatar {
          width: 64rpx;
          height: 64rpx;
          border-radius: 50%;
          margin-right: 16rpx;
        }

        .nickname {
          font-size: 28rpx;
          color: #666;
          font-weight: 500;
        }
      }
    }
  }

  .answers-section {
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      margin-bottom: 20rpx;
      color: #333;
      display: flex;
      align-items: center;
      margin-right: 40rpx;

      .count {
        color: #999;
        margin-left: 10rpx;
        margin-right: auto;
      }

      .edit-profile {
        font-size: 26rpx;
        color: #4facfe;
        font-weight: normal;
        cursor: pointer;

        &:active {
          opacity: 0.8;
        }
      }
    }

    .answer-list {
      .answer-item {
        background: #fff;
        border-radius: 20rpx;
        padding: 30rpx;
        margin-bottom: 20rpx;
        box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

        .answer-header {
          display: flex;
          align-items: center;
          margin-bottom: 20rpx;
          gap: 12rpx;

          .avatar {
            width: 60rpx;
            height: 60rpx;
            border-radius: 30rpx;
            margin-right: 0;
          }

          .nickname {
            font-size: 28rpx;
            color: #333;
            margin-right: auto;
          }

          .time {
            font-size: 24rpx;
            color: #999;
          }
        }

        .answer-content-wrapper {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;

          .answer-content {
            font-size: 28rpx;
            color: #333;
            line-height: 1.6;
            flex: 1;
          }

          .delete-btn {
            padding: 10rpx;
            margin-left: 20rpx;

            .iconfont {
              font-size: 32rpx;
              color: #999;
            }

            &:active {
              opacity: 0.7;
            }
          }
        }

        .reply-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 16rpx;
          margin-top: 20rpx;
          color: #666;
          font-size: 28rpx;

          .iconfont {
            margin-right: 8rpx;
            font-size: 32rpx;
          }

          &:active {
            opacity: 0.7;
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

  .bottom-buttons {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    justify-content: space-between;
    padding: 20rpx 30rpx;
    background: #fff;
    box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);

    button {
      flex: 1;
      height: 80rpx;
      margin: 0 10rpx;
      font-size: 28rpx;
      border-radius: 40rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .iconfont {
        margin-right: 8rpx;
        font-size: 32rpx;
      }

      // 蓝色渐变按钮
      &.share-btn-1 {
        background: linear-gradient(45deg, #4facfe, #00f2fe);
        color: #fff;
      }

      // 蓝色渐变按钮
      &.share-btn-2 {
        background: #f8f9fa;
        color: #333;
      }

      &.comment-btn {
        background: linear-gradient(45deg, #4facfe, #00f2fe);
        color: #fff;
      }

      // 蓝色渐变按钮
      &.play-btn {
        background: linear-gradient(45deg, #4facfe, #00f2fe);
        color: #fff;
      }

      // 默认样式按钮
      &.peek-btn {
        background: #f8f9fa;
        color: #333;
      }

      // 增加按钮间距
      &:not(:last-child) {
        margin-right: 20rpx;
      }
    }
  }

  .comment-modal {
    background: #fff;
    border-radius: 30rpx 30rpx 0 0;
    padding: 30rpx;

    .modal-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .title {
        font-size: 32rpx;
        font-weight: bold;
        width: 500rpx;
      }

      .close {
        font-size: 40rpx;
        color: #999;
        padding: 10rpx;
      }
    }

    .comment-input {
      width: 90%;
      min-height: 200rpx;
      background: #f8f9fa;
      border-radius: 16rpx;
      padding: 20rpx;
      font-size: 28rpx;
      margin-bottom: 20rpx;
    }

    .submit-btn {
      width: 100%;
      height: 80rpx;
      border-radius: 40rpx;
      background: linear-gradient(45deg, #43e97b, #38f9d7);
      color: #fff;
      font-size: 28rpx;

      &:disabled {
        opacity: 0.5;
      }
    }
  }

  .reply-modal {
    background: #fff;
    border-radius: 30rpx 30rpx 0 0;
    padding: 30rpx;

    .modal-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .title {
        font-size: 32rpx;
        font-weight: bold;
        width: 500rpx;
      }

      .close {
        font-size: 40rpx;
        color: #999;
        padding: 10rpx;
      }
    }

    .reply-input {
      width: 90%;
      min-height: 200rpx;
      background: #f8f9fa;
      border-radius: 16rpx;
      padding: 20rpx;
      font-size: 28rpx;
      margin-bottom: 20rpx;
    }

    .submit-btn {
      width: 100%;
      height: 80rpx;
      border-radius: 40rpx;
      background: linear-gradient(45deg, #43e97b, #38f9d7);
      color: #fff;
      font-size: 28rpx;

      &:disabled {
        opacity: 0.5;
      }
    }
  }

  .replies {
    background: #f8f9fa;
    border-radius: 12rpx;
    padding: 20rpx;
    margin-top: 20rpx;

    .reply-item {
      margin-bottom: 16rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .reply-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 10rpx 0;

        .user-info {
          display: flex;
          align-items: center;
          gap: 12rpx;

          .avatar {
            width: 60rpx;
            height: 60rpx;
            border-radius: 30rpx;
            margin-right: 0;
          }

          .nickname {
            font-size: 24rpx;
            color: #666;
            margin-right: 0;
          }

          .reply-content {
            font-size: 26rpx;
            color: #333;
            word-break: break-all;
            margin-left: 10rpx;
          }
        }

        .time {
          font-size: 22rpx;
          color: #999;
        }
      }

      .reply-content-wrapper {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        padding-left: 52rpx;

        .reply-user-content {
          flex: 1;

          .content {
            font-size: 26rpx;
            color: #333;
            word-break: break-all;
          }
        }

        .delete-btn {
          padding: 10rpx;
          margin-left: 20rpx;

          .iconfont {
            font-size: 32rpx;
            color: #999;
          }

          &:active {
            opacity: 0.7;
          }
        }
      }
    }
  }
}

.delete-btn {
  padding: 10rpx;
  margin-left: 20rpx;

  .iconfont {
    font-size: 32rpx;
    color: #999;
  }

  &:active {
    opacity: 0.7;
  }
}

.answer-header,
.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;

  .user-info {
    display: flex;
    align-items: center;
  }
}

.answer-content-wrapper,
.reply-content-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;

  .answer-content,
  .reply-user-content {
    flex: 1;
  }
}

.reply-item {
  margin-bottom: 16rpx;

  &:last-child {
    margin-bottom: 0;
  }

  .reply-header {
    margin-bottom: 8rpx;

    .user-info {
      display: flex;
      align-items: center;

      .avatar {
        width: 40rpx;
        height: 40rpx;
        border-radius: 50%;
        margin-right: 12rpx;
      }

      .nickname {
        font-size: 24rpx;
        color: #666;
        margin-right: 12rpx;
      }

      .time {
        font-size: 22rpx;
        color: #999;
      }
    }
  }

  .reply-content-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding-left: 52rpx;

    .reply-user-content {
      flex: 1;

      .content {
        font-size: 26rpx;
        color: #333;
        word-break: break-all;
      }
    }

    .delete-btn {
      padding: 10rpx;
      margin-left: 20rpx;

      .iconfont {
        font-size: 32rpx;
        color: #999;
      }

      &:active {
        opacity: 0.7;
      }
    }
  }
}

.loading-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  z-index: 100; // 确保loading层级低于弹窗
}

.loading-spinner {
  width: 40rpx;
  height: 40rpx;
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #4facfe;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20rpx;
}

.loading-text {
  font-size: 28rpx;
  color: #666;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.reply-header {
  display: flex;
  align-items: center;
  padding: 10rpx 0;

  .user-info {
    display: flex;
    align-items: center;

    .avatar {
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      margin-right: 12rpx;
    }
  }
}

.reply-peek-btn {
  height: 48rpx;
  padding: 0 20rpx;
  font-size: 24rpx;
  border-radius: 24rpx;
  background: linear-gradient(45deg, #4facfe, #00f2fe);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  margin-left: 20rpx;
}

.reply-peek-btn .iconfont {
  margin-right: 6rpx;
  font-size: 24rpx;
}

:deep(.uni-popup) {
  z-index: 999 !important; // 确保弹窗层级高于loading
}
</style>
