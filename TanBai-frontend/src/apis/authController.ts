/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 获取当前登录用户 GET /auth/get/login */
export async function getLoginUserVoUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultLoginUserVO_>('/auth/get/login', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 微信登录 POST /auth/wx/login */
export async function wxLoginUsingPost({
  body,
  options,
}: {
  body: API.WxLoginDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultLoginUserVO_>('/auth/wx/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
