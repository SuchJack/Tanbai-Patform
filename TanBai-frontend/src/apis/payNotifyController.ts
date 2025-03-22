/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** paySuccessNotify GET /notify/paySuccess */
export async function paySuccessNotifyUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/notify/paySuccess', {
    method: 'GET',
    ...(options || {}),
  });
}

/** paySuccessNotify PUT /notify/paySuccess */
export async function paySuccessNotifyUsingPut({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/notify/paySuccess', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** paySuccessNotify POST /notify/paySuccess */
export async function paySuccessNotifyUsingPost({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/notify/paySuccess', {
    method: 'POST',
    ...(options || {}),
  });
}

/** paySuccessNotify DELETE /notify/paySuccess */
export async function paySuccessNotifyUsingDelete({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/notify/paySuccess', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** paySuccessNotify PATCH /notify/paySuccess */
export async function paySuccessNotifyUsingPatch({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<unknown>('/notify/paySuccess', {
    method: 'PATCH',
    ...(options || {}),
  });
}
