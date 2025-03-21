/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** sendActivityNotice POST /wx/subscribe-message/send-activity-notice */
export async function sendActivityNoticeUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.sendActivityNoticeUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultBoolean_>(
    '/wx/subscribe-message/send-activity-notice',
    {
      method: 'POST',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** sendCommentNotice POST /wx/subscribe-message/send-comment-notice */
export async function sendCommentNoticeUsingPost({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.sendCommentNoticeUsingPOSTParams;
  options?: { [key: string]: unknown };
}) {
  return request<API.ResultBoolean_>(
    '/wx/subscribe-message/send-comment-notice',
    {
      method: 'POST',
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
