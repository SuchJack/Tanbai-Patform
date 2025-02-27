/**
 * 申请订阅消息授权
 * @param templateIds 模板ID数组
 */
export const requestSubscribeMessage = (templateIds: string[]) => {
  return new Promise((resolve, reject) => {
    uni.requestSubscribeMessage({
      tmplIds: templateIds,
      success: (res) => {
        // 检查是否所有模板都被接受
        const results = templateIds.map((id) => res[id])
        if (results.every((result) => result === 'accept')) {
          resolve('全部订阅成功')
        } else if (results.every((result) => result === 'reject')) {
          reject('全部订阅被拒绝')
        } else {
          resolve('部分订阅成功')
        }
      },
      fail: (err) => {
        reject(err)
      },
    })
  })
}
