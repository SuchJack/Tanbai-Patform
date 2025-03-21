/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** 更新用户信息 PUT /users/info */
export async function updateUserInfoUsingPut({
  body,
  options,
}: {
  body: API.UserUpdateDTO;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultLoginUserVO_>('/users/info', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
