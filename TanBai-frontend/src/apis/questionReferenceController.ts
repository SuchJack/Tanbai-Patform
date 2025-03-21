/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 获取所有参考问题 GET /references */
export async function getAllReferencesUsingGet1({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListQuestionReference_>('/references', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据类别获取参考问题 GET /references/category/${param0} */
export async function getReferencesByCategoryUsingGet1({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getReferencesByCategoryUsingGET1Params;
  options?: { [key: string]: unknown };
}) {
  const { category: param0, ...queryParams } = params;

  return request<API.ResultListQuestionReference_>(
    `/references/category/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  );
}

/** 随机获取参考问题 GET /references/random */
export async function getRandomReferencesUsingGet1({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.getRandomReferencesUsingGET1Params;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultListQuestionReference_>('/references/random', {
    method: 'GET',
    params: {
      // count has a default value: 5
      count: '5',
      ...params,
    },
    ...(options || {}),
  });
}
