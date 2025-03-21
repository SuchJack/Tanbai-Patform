/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 生成海报并返回base64 GET /posters/generate/v3/${param0} */
export async function generatePosterBase64UsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.generatePosterBase64UsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { questionId: param0, ...queryParams } = params;

  return request<API.ResultString_>(`/posters/generate/v3/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 根据参数生成海报并返回base64 GET /posters/generate/v4 */
export async function generateCustomPosterBase64UsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.generateCustomPosterBase64UsingGETParams;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultString_>('/posters/generate/v4', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
