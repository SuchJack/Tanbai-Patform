/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** 创建问题 POST /questions */
export async function createQuestionUsingPost({
  body,
  options,
}: {
  body: API.QuestionDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultQuestion_>('/questions', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除问题 DELETE /questions/${param0} */
export async function deleteQuestionUsingDelete({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.deleteQuestionUsingDELETEParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/questions/${param0}`, {
    method: 'DELETE',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 获取问题详情 GET /questions/${param0}/detail */
export async function getQuestionDetailUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getQuestionDetailUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultQuestionDetailVO_>(`/questions/${param0}/detail`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 支付查看权限 POST /questions/${param0}/pay */
export async function payForViewPermissionUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.payForViewPermissionUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/questions/${param0}/pay`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 支付查看回复权限 POST /questions/${param0}/pay/reply */
export async function payForViewReplyPermissionUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.payForViewReplyPermissionUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/questions/${param0}/pay/reply`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 获取我创建的问题列表 GET /questions/my */
export async function getMyQuestionsUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListQuestionWithAnswerCountVO_>('/questions/my', {
    method: 'GET',
    ...(options || {}),
  });
}
