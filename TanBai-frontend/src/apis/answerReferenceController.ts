/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 获取所有参考回答 GET /answer/reference */
export async function getAllReferencesUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListAnswerReference_>('/answer/reference', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据类别获取参考回答 GET /answer/reference/category/${param0} */
export async function getReferencesByCategoryUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getReferencesByCategoryUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  const { category: param0, ...queryParams } = params;

  return request<API.ResultListAnswerReference_>(
    `/answer/reference/category/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 随机获取参考回答 GET /answer/reference/random */
export async function getRandomReferencesUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getRandomReferencesUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListAnswerReference_>('/answer/reference/random', {
    method: 'GET',
    params: {
      // count has a default value: 5
      count: '5',
      ...params,
    },
    ...(options || {}),
  });
}
