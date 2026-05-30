import http from '@ohos.net.http'
import { GLMResponse } from '../model/ChatMessage'
import { Vehicle, statusText } from '../model/Vehicle'
import PrefsUtil from '../common/PrefsUtil'
import { ApiConfig } from '../common/Constants'

interface ChatMessageItem {
  role: string
  content: string
}

let conversationHistory: ChatMessageItem[] = []

export function clearHistory() {
  conversationHistory = []
}

export function buildSystemPrompt(vehicles: Vehicle[]): string {
  const vehicleList = vehicles.map(v =>
    `${v.id}: ${v.type}, ${statusText(v.status)}, 电量${v.battery}%, ${v.loc}, ${v.task}, 今日已跑${v.todayKm}km, 今日完成${v.todayOrders}单, 百公里能耗${v.energyPer100km}kWh, 累计里程${v.totalKm}km`
  ).join('\n')

  const idle = vehicles.filter(v => v.status === 'idle')
  const busy = vehicles.filter(v => v.status === 'busy')
  const charging = vehicles.filter(v => v.status === 'charge')
  const fault = vehicles.filter(v => v.status === 'fault')
  const lowBat = vehicles.filter(v => v.battery < 30)
  const totalOrders = vehicles.reduce((a, v) => a + v.todayOrders, 0)
  const totalKm = vehicles.reduce((a, v) => a + v.todayKm, 0)
  const avgEnergy = (vehicles.reduce((a, v) => a + v.energyPer100km, 0) / vehicles.length).toFixed(1)

  return `你是"轻行Claw"，一个无人车队AI智能体。你的核心职责是主动帮运营人员完成工作。

===== 核心行为准则 =====
1. 你要先干活：收到指令后，你自己去查数据、分析情况、得出结论
2. 用对话告诉用户结果：像真人助手一样，把查到的情况用自然语言说清楚
3. 最后才问确认：只有在需要实际执行操作时，才让用户确认
4. 数据都是现成的：下面提供的数据就是当前系统的真实数据，直接用来回答就行

===== 当前实时车队数据 =====
${vehicleList}

概况：
- 总车辆：${vehicles.length}辆
- 待命：${idle.length}辆（${idle.map(v => v.id).join('、')}）
- 配送中：${busy.length}辆（${busy.map(v => v.id).join('、')}）
- 充电中：${charging.length}辆（${charging.map(v => v.id).join('、')}）
- 故障：${fault.length}辆（${fault.map(v => v.id).join('、')}）
- 低电量(<30%)：${lowBat.length}辆（${lowBat.map(v => v.id + ' ' + v.battery + '%').join('、')}）
- 平均电量：${Math.round(vehicles.reduce((a, v) => a + v.battery, 0) / vehicles.length)}%

===== 今日运营数据 =====
- 今日总配送单量：${totalOrders}单
- 今日总里程：${totalKm}km
- 完成率：98.2%
- 平均时效：23分钟/单
- 较昨日：+12.3%
- 平均百公里能耗：${avgEnergy}kWh

===== 回复格式 =====
你必须返回严格JSON格式（不要加markdown代码块标记）：

对于需要执行操作的指令：
{
  "reply": "用自然语言详细告诉用户情况和建议",
  "need_confirm": true,
  "action_summary": "一句话总结操作",
  "affected_vehicles": ["DM-01", "DM-02"],
  "action_type": "调度|充电|开门|维修|其他"
}

对于纯查询或闲聊：
{
  "reply": "用自然语言详细回答",
  "need_confirm": false
}

===== 关键规则 =====
- 你必须基于上面提供的数据来回答
- 绝对不要说"没有数据""无法查询"这类话
- 调度类指令必须给出完整方案
- 电量低于30%的车辆需要关注
- 故障车辆不能被调度
- 配送中的车辆不能被调度`
}

export async function callGLM(userMessage: string, vehicles: Vehicle[]): Promise<GLMResponse> {
  conversationHistory.push({ role: 'user', content: userMessage })

  const systemPrompt = buildSystemPrompt(vehicles)
  // 把 system prompt 和用户消息合并成一条发给后端
  const fullMessage = `${systemPrompt}\n\n用户消息：${userMessage}`

  try {
    const token = await PrefsUtil.getToken()
    const httpRequest = http.createHttp()
    const headers: Record<string, string> = {
      'Content-Type': 'application/json'
    }
    if (token) {
      headers['Authorization'] = `Bearer ${token}`
    }

    const response = await httpRequest.request(
      `${ApiConfig.BASE_URL}/ai/chat`,
      {
        method: http.RequestMethod.POST,
        header: headers,
        extraData: JSON.stringify({ message: fullMessage }),
        connectTimeout: ApiConfig.TIMEOUT,
        readTimeout: 60000
      }
    )
    httpRequest.destroy()

    if (response.responseCode !== 200) {
      return { reply: '', need_confirm: false, error: `请求失败 ${response.responseCode}` }
    }

    const result = JSON.parse(response.result as string) as Record<string, Object>
    // 后端返回格式: { code: 200, data: { reply: "...", conversationId: ... } }
    const data = result['data'] as Record<string, string>
    const replyContent = (data !== undefined ? (data['reply'] || data['content']) : '') || (result['reply'] as string) || ''

    conversationHistory.push({ role: 'assistant', content: replyContent })

    try {
      let cleanContent = replyContent.replace(/```json\s*/g, '').replace(/```\s*/g, '').trim()
      const jsonMatch = cleanContent.match(/\{[\s\S]*\}/)
      if (jsonMatch) {
        return JSON.parse(jsonMatch[0]) as GLMResponse
      }
    } catch (e) {
    }

    return { reply: replyContent, need_confirm: false }
  } catch (err) {
    return { reply: '', need_confirm: false, error: `${err}` }
  }
}
