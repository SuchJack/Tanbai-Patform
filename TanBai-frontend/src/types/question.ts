export interface Question {
  id: number
  content: string
  creatorId: number
  createTime: string
  updateTime: string
  answerCount?: number
}

export interface QuestionReference {
  id: number
  content: string
  category: string
  sortOrder: number
  createTime: string
  updateTime: string
}
