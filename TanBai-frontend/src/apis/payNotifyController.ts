/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** 支付回调通知处理 POST /notify/paySuccess */
export async function parseOrderNotifyResultUsingPost({
  body,
  options,
}: {
  body: string;
  options?: { [key: string]: unknown };
}) {
  return request<string>('/notify/paySuccess', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
