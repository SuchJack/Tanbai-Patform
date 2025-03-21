/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 创建回复 POST /replies */
export async function createReplyUsingPost({
  body,
  options,
}: {
  body: API.ReplyDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultReplyVO_>('/replies', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除回复 DELETE /replies/${param0} */
export async function deleteReplyUsingDelete({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.deleteReplyUsingDELETEParams;
  options?: { [key: string]: unknown };
}) {
  const { replyId: param0, ...queryParams } = params;

  return request<API.ResultBoolean_>(`/replies/${param0}`, {
    method: 'DELETE',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}
