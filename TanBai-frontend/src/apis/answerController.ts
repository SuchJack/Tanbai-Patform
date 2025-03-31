/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** 创建回答 POST /answers */
export async function createAnswerUsingPost({
  body,
  options,
}: {
  body: API.AnswerDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultAnswer_>('/answers', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除回答 DELETE /answers/${param0} */
export async function deleteAnswerUsingDelete({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.deleteAnswerUsingDELETEParams;
  options?: { [key: string]: unknown };
}) {
  const { answerId: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/answers/${param0}`, {
    method: 'DELETE',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 获取我参与回答的问题列表 GET /answers/my/${param0} */
export async function getMyAnswersUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getMyAnswersUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { userId: param0, ...queryParams } = params;

  return request<API.ResultListAnswerVO_>(`/answers/my/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 获取问题的所有回答 GET /answers/question/${param0} */
export async function getAnswersByQuestionUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getAnswersByQuestionUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultListAnswer_>(`/answers/question/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
