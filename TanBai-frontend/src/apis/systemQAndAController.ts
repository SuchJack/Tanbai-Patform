/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 获取所有常见问题 GET /qa */
export async function getAllQAndAUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListSystemQAndA_>('/qa', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 搜索常见问题 GET /qa/search */
export async function searchQAndAUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.searchQAndAUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListSystemQAndA_>('/qa/search', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
