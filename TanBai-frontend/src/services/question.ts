import { http } from '@/utils/http'

/**
 * 创建问题
 * @param data
 */
export const createQuestionAPI = (data: { userStore: any; content: any }) => {
  return http({
    url: '/questions',
    method: 'POST',
    data: {
      content: data.content,
      creatorId: data.userStore.profile.userId,
    },
  })
}

/**
 * 获取参考问题
 * @param data
 */
export const getReferencesAPI = (data: { userStore: any }) => {
  return http({
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
export const getMyQuestionAPI = () => {
  return http({
    url: `/questions/my`,
    method: 'GET',
  })
}

/**
 * 获取我的参与问题
 * @param data
 */
export const getMyParticipateQuestionsAPI = (data: { userId: any }) => {
  return http({
    url: `/answers/my/${data.userId}`,
    method: 'GET',
  })
}

/**
 * 获取问题详情
 * @param data
 */
export const getQuestionDetailAPI = (data: { questionId: any }) => {
  return http({
    url: `/questions/${data.questionId}/detail`,
    method: 'GET',
  })
}

/**
 * 删除问题
 * @param data
 */
export const deleteQuestionAPI = (data: { questionId: any; userStore: any }) => {
  return http({
    url: `/questions/${data.questionId}?userId=${data.userStore.profile?.userId}`,
    method: 'DELETE',
  })
}

/**
 * 提交回答
 * @param data
 */
export const submitCommentAPI = (data: { questionId: any; content: any; userStore: any }) => {
  return http({
    url: '/answers',
    method: 'POST',
    data: {
      content: data.content,
      questionId: data.questionId,
      userId: data.userStore.profile?.userId,
    },
  })
}

/**
 * 回复回答
 * @param data
 */
export const replyCommentAPI = (data: { answerId: any; content: any; userStore: any }) => {
  return http({
    url: '/replies',
    method: 'POST',
    data: {
      answerId: data.answerId,
      content: data.content,
      userId: data.userStore.profile?.userId,
    },
  })
}

/**
 * 删除评论
 * @param data
 */
export const deleteCommentAPI = (data: { commentId: any; userStore: any; isCreator: any }) => {
  return http({
    url: `/answers/${data.commentId}?userId=${data.userStore.profile?.userId}`,
    method: 'DELETE',
    data: {
      userId: data.userStore.profile?.userId,
      isCreator: data.isCreator, // 传递是否为创建者的标识
    },
  })
}

/**
 * 删除回复
 * @param data
 */
export const deleteReplyAPI = (data: { replyId: any; userStore: any; isCreator: any }) => {
  return http({
    url: `/replies/${data.replyId}?userId=${data.userStore.profile?.userId}`,
    method: 'DELETE',
    data: {
      userId: data.userStore.profile?.userId,
      isCreator: data.isCreator, // 传递是否为创建者的标识
    },
  })
}
