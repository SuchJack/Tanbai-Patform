/**
 * 获取随机参考回答
 */
export const getRandomReferencesAPI = (count: number = 5) => {
  return uni.request({
    url: `/answer/reference/random?count=${count}`,
    method: 'GET',
  })
}
