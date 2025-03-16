<template>
  <view class="agreement">
    <view class="header">
      <text class="title">用户协议</text>
    </view>

    <view class="content">
      <view class="section">
        <!--        <text class="section-title">1. 协议的范围</text>-->
        <text class="section-content">
          1.本程序是一个提供朋友之间友好互动的工具。我们提倡真诚、友好、健康的内容和互动，坚决反对涉黄、涉暴、涉政的内容，坚决反对诽谤他人、辱骂他人、骚扰他人、攻击他人。坚决反对泄露他人隐私、侵害他人合法权益。对违规者将予以永久封禁处理，严重者将移交警察机关。
        </text>
        <text class="section-content">
          2.严禁未成年人使用，严禁未成年人使用，严禁未成年人使用。
        </text>
        <text class="section-content">
          3.本程序保护数据的安全性和用户的隐私，数据不会泄露给第三方。在用户的同意的前提下只会获取获取用户的头像和昵称。我们没有权限获取用户姓名等其它个人信息。
        </text>
        <text class="section-content">
          4.用户之间若发生纠纷、损失（情感和利益等），盖由用户自行负责承担，本程序及对应公司无须承担任何的法律责任。
        </text>
        <text class="section-content">
          5.本程序数据绝对真实，系统不会在程序内给大家留言，更不会伪装假冒其它用户留言。
        </text>
        <text class="section-content">
          6.解锁之后只会显示微信头像和昵称，不能显示对方的姓名。有用户反馈解锁后头像和昵称无法在微信好友中找到，原因有2种：一是用户更换了头像和昵称，二是别人分享了你的二维码，留言的是别人的朋友。有的用户头像设置是透明，所以头像也为空。
        </text>
        <text class="section-content">
          7.提问者（或留言者）不能删除提问（或留言）。主人可删除提问（或留言），但删除后不能恢复。
        </text>
        <text class="section-content">
          8.访问或使用本产品服务，即视为你已同意接受此用户协议和隐私条款。
        </text>
      </view>

      <!--      <view class="section">-->
      <!--        <text class="section-title">2. 服务内容</text>-->
      <!--        <text class="section-content">-->
      <!--          我们为用户提供以下服务： • 问答互动 • 知识分享 • 社区交流-->
      <!--        </text>-->
      <!--      </view>-->

      <!--      <view class="section">-->
      <!--        <text class="section-title">3. 用户行为规范</text>-->
      <!--        <text class="section-content">-->
      <!--          用户在使用本平台服务时应遵守中华人民共和国相关法律法规，不得利用本平台服务从事违法违规行为。-->
      <!--        </text>-->
      <!--      </view>-->

      <!--      <view class="section">-->
      <!--        <text class="section-title">4. 隐私保护</text>-->
      <!--        <text class="section-content">-->
      <!--          我们重视用户的隐私保护，具体隐私政策请参见《隐私政策》。-->
      <!--        </text>-->
      <!--      </view>-->
    </view>
  </view>
</template>

<style lang="scss">
.agreement {
  padding: 30rpx;
  min-height: 100vh;
  background: #fff;

  .header {
    margin-bottom: 40rpx;

    .title {
      font-size: 48rpx;
      font-weight: bold;
      color: #333;
    }
  }

  .content {
    .section {
      margin-bottom: 30rpx;

      .section-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 16rpx;
        display: block;
      }

      .section-content {
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
        display: block;
        margin: 20rpx 0;
      }
    }
  }
}
</style>
