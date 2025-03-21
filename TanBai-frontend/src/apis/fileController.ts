/* eslint-disable */
// @ts-ignore
import { request } from 'axios';

import * as API from './types';

/** testDownloadFile GET /file/test/download/ */
export async function testDownloadFileUsingGet({
  params,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.testDownloadFileUsingGETParams;
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/file/test/download/', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** testUploadFile POST /file/test/upload */
export async function testUploadFileUsingPost({
  body,
  file,
  options,
}: {
  body: {};
  file?: File;
  options?: { [key: string]: unknown };
}) {
  const formData = new FormData();

  if (file) {
    formData.append('file', file);
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as { [key: string]: any })[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''));
        } else {
          formData.append(ele, JSON.stringify(item));
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<API.ResultString_>('/file/test/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: formData,
    ...(options || {}),
  });
}

/** uploadFile POST /file/upload */
export async function uploadFileUsingPost({
  params,
  body,
  file,
  options,
}: {
  // 叠加生成的Param类型 (非body参数openapi默认没有生成对象)
  params: API.uploadFileUsingPOSTParams;
  body: {};
  file?: File;
  options?: { [key: string]: unknown };
}) {
  const formData = new FormData();

  if (file) {
    formData.append('file', file);
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as { [key: string]: any })[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''));
        } else {
          formData.append(ele, JSON.stringify(item));
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<API.ResultString_>('/file/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    params: {
      ...params,
    },
    data: formData,
    ...(options || {}),
  });
}
