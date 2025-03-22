/* eslint-disable */
// @ts-ignore
import request from '@/utils/request.ts';

import * as API from './types';

/** getCustomerServiceInfo GET /customer-service */
export async function getCustomerServiceInfoUsingGet({
  options,
}: {
  options?: { [key: string]: unknown };
}) {
  return request<API.CustomerService>('/customer-service', {
    method: 'GET',
    ...(options || {}),
  });
}
