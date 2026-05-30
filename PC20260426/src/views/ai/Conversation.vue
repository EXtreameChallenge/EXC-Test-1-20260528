<template>
  <div class="ai-conversation">
    <div class="chat-layout">
      <div class="chat-main">
        <div class="chat-messages" ref="messagesRef">
          <div v-for="msg in aiStore.messages" :key="msg.id" class="message" :class="msg.role">
            <div class="msg-avatar">{{ msg.role === 'ai' ? '🤖' : '👤' }}</div>
            <div class="msg-bubble">{{ msg.content }}</div>
          </div>
          <div v-if="aiStore.aiTyping" class="message ai">
            <div class="msg-avatar">🤖</div>
            <div class="msg-bubble typing">思考中...</div>
          </div>
        </div>
        <div class="chat-input">
          <el-input v-model="aiStore.inputText" placeholder="输入消息..." @keydown.enter="handleSend" />
          <el-button type="primary" @click="handleSend" :loading="aiStore.aiTyping">发送</el-button>
        </div>
      </div>
      <div class="chat-sidebar">
        <h3>历史对话</h3>
        <div v-for="item in aiStore.history" :key="item.id" class="history-item" @click="aiStore.loadHistory(item)">
          <span class="history-title">{{ item.title }}</span>
          <span class="history-time">{{ item.time }}</span>
        </div>
        <el-button text size="small" @click="aiStore.clearChat" style="margin-top:12px">清空对话</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAIStore } from '../../stores/ai'

const aiStore = useAIStore()
const messagesRef = ref(null)

function handleSend() {
  if (aiStore.inputText.trim()) {
    aiStore.sendMessage(aiStore.inputText)
  }
}
</script>

<style scoped>
.ai-conversation { height: calc(100vh - var(--header-height) - 48px); }
.chat-layout { display: flex; height: 100%; gap: 16px; }
.chat-main { flex: 1; display: flex; flex-direction: column; background: var(--glass); border: 1px solid var(--glass-border); border-radius: var(--border-radius); overflow: hidden; }
.chat-messages { flex: 1; overflow-y: auto; padding: 16px; display: flex; flex-direction: column; gap: 12px; }
.message { display: flex; gap: 8px; max-width: 80%; }
.message.user { align-self: flex-end; flex-direction: row-reverse; }
.msg-avatar { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0; background: var(--glass-heavy); }
.msg-bubble { padding: 10px 14px; border-radius: 12px; font-size: 14px; line-height: 1.5; }
.message.ai .msg-bubble { background: var(--glass-heavy); border: 1px solid var(--glass-border); }
.message.user .msg-bubble { background: linear-gradient(135deg, var(--blue), var(--purple)); color: white; }
.msg-bubble.typing { color: var(--text-muted); font-style: italic; }
.chat-input { display: flex; gap: 8px; padding: 12px; border-top: 1px solid var(--glass-border); }
.chat-sidebar { width: 240px; background: var(--glass); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 16px; overflow-y: auto; }
.chat-sidebar h3 { font-size: 14px; font-weight: 600; margin-bottom: 12px; }
.history-item { padding: 8px 10px; border-radius: 8px; cursor: pointer; transition: var(--transition); margin-bottom: 4px; }
.history-item:hover { background: var(--glass-heavy); }
.history-title { display: block; font-size: 13px; font-weight: 500; }
.history-time { display: block; font-size: 11px; color: var(--text-muted); margin-top: 2px; }
</style>
