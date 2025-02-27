import { http } from '../utils/http'

export const justTestAPI = () => {
  return http({
    url: '/test',
    method: 'GET',
  })
}

/**
 * 创建问题
 * @param data
 */
export const createQuestionAPI = (data: { userStore: any; content: any }) => {
  return uni.request({
    url: '/questions',
    method: 'POST',
    data: {
      content: data.content,
      creatorId: data.userStore.profile.id,
    },
  })
}

/**
 * 获取参考问题
 * @param data
 */
export const getReferencesAPI = (data: { userStore: any }) => {
  return uni.request({
    url: '/references/random',
    method: 'GET',
    data: {
      count: 5,
    },
  })
}

/**
 * 获取我的问题
 * @param data
 */
export const getMyQuestionAPI = (data: { userStore: any }) => {
  return uni.request({
    url: `/questions/my/${data.userStore.profile?.id}`,
    method: 'GET',
    header: {
      Authorization: `Bearer ${data.userStore.profile?.token}`,
    },
  })
}

/**
 * 获取我的参与问题
 * @param data
 */
export const getMyParticipateQuestionsAPI = (data: { userId: any }) => {
  return uni.request({
    url: `/answers/my/${data.userId}`,
    method: 'GET',
  })
}

/**
 * 获取问题详情
 * @param data
 */
export const getQuestionDetailAPI = (data: { questionId: any; userStore: any }) => {
  return uni.request({
    url: `/questions/${data.questionId}/detail?userId=${data.userStore.profile?.id}`,
    method: 'GET',
  })
}

/**
 * 删除问题
 * @param data
 */
export const deleteQuestionAPI = (data: { questionId: any; userStore: any }) => {
  return uni.request({
    url: `/questions/${data.questionId}?userId=${data.userStore.profile?.id}`,
    method: 'DELETE',
  })
}

/**
 * 提交回答
 * @param data
 */
export const submitCommentAPI = (data: { questionId: any; content: any; userStore: any }) => {
  return uni.request({
    url: '/answers',
    method: 'POST',
    data: {
      content: data.content,
      questionId: data.questionId,
      userId: data.userStore.profile?.id,
    },
  })
}

/**
 * 回复回答
 * @param data
 */
export const replyCommentAPI = (data: { answerId: any; content: any; userStore: any }) => {
  return uni.request({
    url: '/replies',
    method: 'POST',
    data: {
      answerId: data.answerId,
      content: data.content,
      userId: data.userStore.profile?.id,
    },
  })
}

/**
 * 删除评论
 * @param data
 */
export const deleteCommentAPI = (data: { commentId: any; userStore: any; isCreator: any }) => {
  return uni.request({
    url: `/answers/${data.commentId}?userId=${data.userStore.profile?.id}`,
    method: 'DELETE',
    data: {
      userId: data.userStore.profile?.id,
      isCreator: data.isCreator, // 传递是否为创建者的标识
    },
  })
}

/**
 * 删除回复
 * @param data
 */
export const deleteReplyAPI = (data: { replyId: any; userStore: any; isCreator: any }) => {
  return uni.request({
    url: `/replies/${data.replyId}?userId=${data.userStore.profile?.id}`,
    method: 'DELETE',
    data: {
      userId: data.userStore.profile?.id,
      isCreator: data.isCreator, // 传递是否为创建者的标识
    },
  })
}
