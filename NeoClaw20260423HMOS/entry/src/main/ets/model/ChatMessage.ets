/**
 * 聊天消息数据模型
 */

export interface ChatMessage {
  role: string;     // 发送者："ai"/"user"
  content: string;  // 消息内容
  time: string;     // 时间格式："HH:MM"
  id?: string;      // 消息ID（可选）
}

export interface GLMResponse {
  reply: string;
  need_confirm: boolean;
  action_summary?: string;
  affected_vehicles?: string[];
  action_type?: string;
  error?: string;
}

// 消息发送者类型
export enum MessageFrom {
  AI = 'ai',
  USER = 'user'
}

export function createMessage(role: string, content: string): ChatMessage {
  const now = new Date();
  const hours = now.getHours().toString().padStart(2, '0');
  const minutes = now.getMinutes().toString().padStart(2, '0');
  return {
    role,
    content,
    time: `${hours}:${minutes}`
  };
}