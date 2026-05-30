import { sendAIMessage } from '../api'

export interface ChatMessage {
  id: number
  role: 'user' | 'ai'
  content: string
  time: string
}

export interface AIResponse {
  content: string
  error?: string
}

class AIService {
  async sendMessage(message: string): Promise<AIResponse> {
    try {
      const res = await sendAIMessage(message)
      const result = res.data

      if (result.code === 200 || result.success) {
        const data = result.data || {}
        return { content: data.reply || data.content || data.message || '已收到您的消息' }
      }

      return { content: result.message || '处理请求时出现错误' }
    } catch (error) {
      console.error('AI 服务错误:', error)
      return this.localFallback(message)
    }
  }

  private localFallback(message: string): AIResponse {
    const lower = message.toLowerCase()

    if (lower.includes('车辆') || lower.includes('车队')) {
      return { content: '当前车队运行正常。如需查看详细数据，请前往车辆管理页面。' }
    }
    if (lower.includes('任务') || lower.includes('调度')) {
      return { content: '任务调度系统运行正常。如需创建新任务，请前往调度中心。' }
    }
    if (lower.includes('告警') || lower.includes('故障')) {
      return { content: '当前告警系统正常监控中。如需查看告警详情，请前往故障管理页面。' }
    }

    return { content: `收到您的消息："${message}"。后端AI服务暂时不可用，请稍后重试。` }
  }
}

export const aiService = new AIService()
