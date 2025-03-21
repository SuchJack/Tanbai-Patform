/* eslint-disable */
// @ts-ignore

export type Answer = {
  content?: string;
  createTime?: string;
  id?: number;
  questionId?: number;
  updateTime?: string;
  userId?: number;
};

export type AnswerDTO = {
  /** 回答内容 */
  content?: string;
  /** 问题ID */
  questionId?: number;
  /** 用户ID */
  userId?: number;
};

export type AnswerReference = {
  category?: string;
  content?: string;
  createTime?: string;
  id?: number;
  isDelete?: number;
  updateTime?: string;
};

export type AnswerVO = {
  answer?: Answer;
  /** 问题内容 */
  questionContent?: string;
  /** 问题创建时间 */
  questionCreateTime?: string;
  /** 问题创建者头像 */
  questionCreatorAvatarUrl?: string;
  /** 问题创建者昵称 */
  questionCreatorNickName?: string;
};

export type AnswerWithRepliesVO = {
  answer?: Answer;
  /** 回复列表 */
  replies?: ReplyVO[];
  /** 回答者头像 */
  userAvatarUrl?: string;
  /** 回答者昵称 */
  userNickName?: string;
};

export type cancelOrderUsingPOSTParams = {
  /** 订单号 */
  orderNumber: string;
  /** 用户ID */
  userId?: number;
};

export type CustomerService = {
  email?: string;
  id?: number;
  wechat?: string;
  workingHours?: string;
};

export type deleteAnswerUsingDELETEParams = {
  /** 回答ID */
  answerId: number;
  /** 当前用户ID */
  userId?: number;
};

export type deleteQuestionUsingDELETEParams = {
  /** 问题ID */
  questionId: number;
  /** 当前用户ID */
  userId?: number;
};

export type deleteReplyUsingDELETEParams = {
  /** 回复ID */
  replyId: number;
  /** 当前用户ID */
  userId?: number;
};

export type generateCustomPosterBase64UsingGETParams = {
  /** 头像URL */
  avatarUrl?: string;
  /** 二维码内容 */
  qrCodeContent?: string;
  /** 副标题 */
  subtitle?: string;
};

export type generatePosterBase64UsingGETParams = {
  /** questionId */
  questionId: string;
};

export type getAnswersByQuestionUsingGETParams = {
  /** 问题ID */
  questionId: number;
};

export type getMyAnswersUsingGETParams = {
  /** 用户ID */
  userId: number;
};

export type getOrderDetailUsingGETParams = {
  /** 订单号 */
  orderNumber: string;
  /** 用户ID */
  userId?: number;
};

export type getOrderPayStatusUsingGETParams = {
  /** orderNumber */
  orderNumber: string;
  /** questionId */
  questionId: number;
};

export type getQuestionDetailUsingGETParams = {
  /** 问题ID */
  questionId: number;
};

export type getRandomReferencesUsingGET1Params = {
  /** 获取数量 */
  count?: number;
};

export type getRandomReferencesUsingGETParams = {
  /** 获取数量 */
  count?: number;
};

export type getReferencesByCategoryUsingGET1Params = {
  /** 问题类别 */
  category: string;
};

export type getReferencesByCategoryUsingGETParams = {
  /** 回答类别 */
  category: string;
};

export type getReplyOrderPayStatusUsingGETParams = {
  /** orderNumber */
  orderNumber: string;
  /** questionId */
  questionId: number;
};

export type getUserOrdersUsingGETParams = {
  /** 用户ID */
  userId: number;
};

export type LoginUserVO = {
  /** 头像URL */
  avatarUrl?: string;
  /** 昵称 */
  nickName?: string;
  /** 用户openid */
  openId?: string;
  /** 认证token */
  tokenValue?: string;
  /** 用户ID */
  userId?: number;
  /** 用户角色 */
  userRole?: string;
};

export type OrderDTO = {
  /** 支付金额 */
  amount?: number;
  /** 支付方式 1微信,2支付宝 */
  payMethod?: number;
  /** 用户ID */
  userId?: number;
};

export type OrderPaymentVO = {
  nonceStr?: string;
  packageStr?: string;
  paySign?: string;
  signType?: string;
  timeStamp?: string;
};

export type OrderVO = {
  /** 实收金额 */
  amount?: number;
  /** 结账时间 */
  checkoutTime?: string;
  /** 订单ID */
  id?: number;
  /** 订单号 */
  number?: string;
  /** 下单时间 */
  orderTime?: string;
  /** 支付方式 1微信,2支付宝 */
  payMethod?: number;
  /** 支付状态 0未支付 1已支付 2退款 */
  payStatus?: number;
  /** 订单状态 1待付款 2已完成 3已取消 4退款 */
  status?: number;
  /** 下单用户 */
  userId?: number;
};

export type PayDTO = {
  /** 用户openId */
  openId?: string;
  /** 订单号 */
  orderNumber?: string;
  /** 用户ID */
  userId?: string;
};

export type payForViewPermissionUsingPOSTParams = {
  /** 问题ID */
  questionId: number;
  /** 当前用户ID */
  userId?: number;
};

export type payForViewReplyPermissionUsingPOSTParams = {
  /** 问题ID */
  questionId: number;
  /** 当前用户ID */
  userId?: number;
};

export type Question = {
  content?: string;
  createTime?: string;
  creatorId?: number;
  id?: number;
  isPaid?: number;
  seeReply?: number;
  updateTime?: string;
};

export type QuestionDetailVO = {
  /** 回答列表 */
  answers?: AnswerWithRepliesVO[];
  question?: QuestionVO;
};

export type QuestionDTO = {
  /** 问题内容 */
  content?: string;
  /** 创建者ID */
  creatorId?: number;
};

export type QuestionReference = {
  category?: string;
  content?: string;
  createTime?: string;
  id?: number;
  sortOrder?: number;
  updateTime?: string;
};

export type QuestionVO = {
  /** 问题内容 */
  content?: string;
  /** 创建时间 */
  createTime?: string;
  /** 创建者头像 */
  creatorAvatarUrl?: string;
  /** 创建者ID */
  creatorId?: number;
  /** 创建者昵称 */
  creatorNickName?: string;
  /** 问题ID */
  id?: number;
};

export type QuestionWithAnswerCountVO = {
  /** 回答数量 */
  answerCount?: number;
  question?: Question;
};

export type refundOrderUsingPOSTParams = {
  /** 订单号 */
  orderNumber: string;
  /** 用户ID */
  userId?: number;
};

export type ReplyDTO = {
  /** 回答ID */
  answerId?: number;
  /** 回复内容 */
  content?: string;
  /** 用户ID */
  userId?: number;
};

export type ReplyVO = {
  /** 回答ID */
  answerId?: number;
  /** 回复内容 */
  content?: string;
  /** 创建时间 */
  createTime?: string;
  /** 回复ID */
  id?: number;
  /** 回复者头像 */
  userAvatarUrl?: string;
  /** 回复者ID */
  userId?: number;
  /** 回复者昵称 */
  userNickName?: string;
};

export type ResultAnswer_ = {
  code?: number;
  data?: Answer;
  message?: string;
};

export type ResultBoolean_ = {
  code?: number;
  data?: boolean;
  message?: string;
};

export type ResultListAnswer_ = {
  code?: number;
  data?: Answer[];
  message?: string;
};

export type ResultListAnswerReference_ = {
  code?: number;
  data?: AnswerReference[];
  message?: string;
};

export type ResultListAnswerVO_ = {
  code?: number;
  data?: AnswerVO[];
  message?: string;
};

export type ResultListOrderVO_ = {
  code?: number;
  data?: OrderVO[];
  message?: string;
};

export type ResultListQuestionReference_ = {
  code?: number;
  data?: QuestionReference[];
  message?: string;
};

export type ResultListQuestionWithAnswerCountVO_ = {
  code?: number;
  data?: QuestionWithAnswerCountVO[];
  message?: string;
};

export type ResultListSystemQAndA_ = {
  code?: number;
  data?: SystemQAndA[];
  message?: string;
};

export type ResultLoginUserVO_ = {
  code?: number;
  data?: LoginUserVO;
  message?: string;
};

export type ResultOrderPaymentVO_ = {
  code?: number;
  data?: OrderPaymentVO;
  message?: string;
};

export type ResultOrderVO_ = {
  code?: number;
  data?: OrderVO;
  message?: string;
};

export type ResultQuestion_ = {
  code?: number;
  data?: Question;
  message?: string;
};

export type ResultQuestionDetailVO_ = {
  code?: number;
  data?: QuestionDetailVO;
  message?: string;
};

export type ResultReplyVO_ = {
  code?: number;
  data?: ReplyVO;
  message?: string;
};

export type ResultString_ = {
  code?: number;
  data?: string;
  message?: string;
};

export type searchQAndAUsingGETParams = {
  /** 搜索关键词 */
  keyword?: string;
};

export type sendActivityNoticeUsingPOSTParams = {
  /** content */
  content: string;
  /** name */
  name: string;
  /** openId */
  openId: string;
  /** title */
  title: string;
};

export type sendCommentNoticeUsingPOSTParams = {
  /** content */
  content: string;
  /** openId */
  openId: string;
  /** tips */
  tips: string;
};

export type SystemQAndA = {
  answer?: string;
  createTime?: string;
  id?: number;
  question?: string;
  sortOrder?: number;
  updateTime?: string;
};

export type testDownloadFileUsingGETParams = {
  /** filepath */
  filepath?: string;
};

export type uploadFileUsingPOSTParams = {
  biz?: string;
  openId?: string;
};

export type UserUpdateDTO = {
  /** 头像URL */
  avatarUrl?: string;
  /** 昵称 */
  nickName?: string;
  /** 用户ID */
  userId?: number;
};

export type WxLoginDTO = {
  /** 微信登录code */
  code?: string;
};
