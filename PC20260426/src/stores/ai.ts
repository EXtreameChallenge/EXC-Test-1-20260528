import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { aiService, type ChatMessage } from '../services/aiService'

export interface ConversationHistory {
  id: number
  title: string
  time: string
  messages: ChatMessage[]
}

export const useAIStore = defineStore('ai', () => {
  const messages = ref<ChatMessage[]>([
    {
      id: 1,
      role: 'ai',
      content: '您好！我是轻行 Claw 智能体，可以帮您管理无人车调度、查看运营数据、处理故障告警等。请问有什么可以帮您的？',
      time: '10:00'
    }
  ])
  const inputText = ref('')
  const aiTyping = ref(false)
  const history = ref<ConversationHistory[]>([
    { id: 1, title: '车队状态查询', time: '今天 09:30', messages: [] },
    { id: 2, title: '配送任务创建', time: '昨天 14:20', messages: [] },
    { id: 3, title: '故障告警处理', time: '昨天 10:15', messages: [] }
  ])

  const messageCount = computed(() => messages.value.length)

  function getCurrentTime(): string {
    const now = new Date()
    return `${now.getHours()}:${String(now.getMinutes()).padStart(2, '0')}`
  }

  async function sendMessage(text: string) {
    if (!text?.trim()) return

    const userMessage: ChatMessage = {
      id: Date.now(),
      role: 'user',
      content: text.trim(),
      time: getCurrentTime()
    }
    messages.value.push(userMessage)
    inputText.value = ''
    aiTyping.value = true

    try {
      const response = await aiService.sendMessage(text.trim())

      const aiMessage: ChatMessage = {
        id: Date.now() + 1,
        role: 'ai',
        content: response.content,
        time: getCurrentTime()
      }
      messages.value.push(aiMessage)
    } catch (error) {
      console.error('发送消息失败:', error)
      const errorMessage: ChatMessage = {
        id: Date.now() + 1,
        role: 'ai',
        content: '抱歉，处理您的请求时出现了错误，请稍后重试。',
        time: getCurrentTime()
      }
      messages.value.push(errorMessage)
    } finally {
      aiTyping.value = false
    }
  }

  function clearChat() {
    messages.value = [{
      id: 1,
      role: 'ai',
      content: '对话已清空。请问有什么可以帮您的？',
      time: getCurrentTime()
    }]
  }

  function loadHistory(historyItem: ConversationHistory) {
    messages.value = historyItem.messages.length > 0
      ? [...historyItem.messages]
      : [{
          id: 1,
          role: 'ai',
          content: `已加载历史对话：${historyItem.title}`,
          time: getCurrentTime()
        }]
  }

  return {
    messages,
    inputText,
    aiTyping,
    history,
    messageCount,
    sendMessage,
    clearChat,
    loadHistory
  }
})
