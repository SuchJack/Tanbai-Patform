import type { GenerateServiceProps } from 'openapi-ts-request'

export default {
  // schemaPath: './openapi.json', // 本地openapi文件
  // serversPath: './src/apis', // 接口存放路径
  schemaPath: 'http://localhost:8641/v2/api-docs',
} as GenerateServiceProps
