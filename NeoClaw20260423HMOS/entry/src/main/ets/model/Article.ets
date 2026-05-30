// 文章数据模型定义

export interface Article {
  id: number
  title: string
  content: string
  fullContent: string
  image: string
  author: string
  likes: number
  comments: number
  tags: string[]
  isLiked: boolean
  isCollected: boolean
}

// 创建默认文章数据
export function createDefaultArticles(): Article[] {
  return [
    {
      id: 1,
      title: '轻行无人车操作全攻略',
      content: '掌握轻行无人车的基本操作技巧，从启动到完成任务的全流程指导...',
      fullContent: '轻行无人车操作全攻略\n\n1. 车辆启动流程\n- 检查车辆电量是否充足（建议>50%）\n- 确认传感器状态正常\n- 通过APP远程启动车辆\n\n2. 任务执行流程\n- 在APP端设置配送目的地\n- 系统自动规划最优路线\n- 实时监控车辆运行状态\n\n3. 异常情况处理\n- 电量不足自动返回充电\n- 遇到障碍物自动避障\n- 网络中断自动暂停任务',
      image: '',
      author: '轻行官方',
      likes: 128,
      comments: 45,
      tags: ['操作指南', '新手必看'],
      isLiked: false,
      isCollected: false
    },
    {
      id: 2,
      title: '智能调度算法解析',
      content: '了解轻行Claw背后的智能调度算法如何优化配送效率...',
      fullContent: '智能调度算法解析\n\n轻行Claw采用先进的AI调度算法：\n\n1. 实时路况分析\n- 基于历史数据和实时交通信息\n- 动态调整配送路线\n- 避开拥堵路段\n\n2. 车辆负载均衡\n- 根据车辆电量、容量智能分配任务\n- 确保每辆车工作量均衡\n- 延长电池使用寿命\n\n3. 紧急任务优先\n- 识别高优先级订单\n- 自动调整调度策略\n- 确保时效性要求',
      image: '',
      author: '技术团队',
      likes: 96,
      comments: 32,
      tags: ['技术解析', '算法'],
      isLiked: false,
      isCollected: false
    },
    {
      id: 3,
      title: '电池维护保养指南',
      content: '延长无人车电池寿命的专业建议和维护方法...',
      fullContent: '电池维护保养指南\n\n1. 充电最佳实践\n- 建议电量保持在20%-80%之间\n- 避免深度放电（<10%）\n- 定期进行完整充放电校准\n\n2. 温度管理\n- 适宜工作温度：0°C-40°C\n- 避免高温暴晒\n- 冬季注意电池保温\n\n3. 日常检查项目\n- 每周检查电池连接线\n- 每月检查电池外观\n- 每季度专业检测',
      image: '',
      author: '运维专家',
      likes: 87,
      comments: 28,
      tags: ['维护保养', '电池'],
      isLiked: false,
      isCollected: false
    },
    {
      id: 4,
      title: '配送效率提升技巧',
      content: '如何通过合理配置提升无人车配送效率的实用技巧...',
      fullContent: '配送效率提升技巧\n\n1. 路线优化策略\n- 批量处理相邻区域订单\n- 避开高峰时段配送\n- 利用夜间进行补货配送\n\n2. 车辆配置优化\n- 根据货物类型选择合适的车型\n- 合理分配载重容量\n- 动态调整车辆数量\n\n3. 数据监控分析\n- 实时监控配送时效\n- 分析瓶颈环节\n- 持续优化调度策略',
      image: '',
      author: '运营总监',
      likes: 112,
      comments: 41,
      tags: ['效率提升', '运营'],
      isLiked: false,
      isCollected: false
    },
    {
      id: 5,
      title: '安全操作规范',
      content: '无人车安全操作的重要规范和注意事项...',
      fullContent: '安全操作规范\n\n1. 操作前检查\n- 确认车辆状态正常\n- 检查传感器校准\n- 验证通信连接稳定\n\n2. 运行中监控\n- 实时关注车辆位置\n- 监控电池电量变化\n- 注意天气条件影响\n\n3. 紧急情况处理\n- 掌握紧急停止操作\n- 了解故障代码含义\n- 及时联系技术支持',
      image: '',
      author: '安全专家',
      likes: 73,
      comments: 19,
      tags: ['安全规范', '操作'],
      isLiked: false,
      isCollected: false
    },
    {
      id: 6,
      title: '车队规模规划建议',
      content: '根据业务需求合理规划无人车车队规模的实用建议...',
      fullContent: '车队规模规划建议\n\n1. 需求分析\n- 日均配送订单量评估\n- 配送区域覆盖范围\n- 高峰时段需求分析\n\n2. 车辆配置计算\n- 基础车辆数 = 日均订单/单车日产能\n- 备用车辆数 = 基础车辆数 × 20%\n- 维护车辆数 = 基础车辆数 × 10%\n\n3. 动态调整策略\n- 根据季节性需求调整\n- 考虑业务增长预留\n- 定期评估优化配置',
      image: '',
      author: '规划顾问',
      likes: 64,
      comments: 22,
      tags: ['规划', '车队管理'],
      isLiked: false,
      isCollected: false
    }
  ]
}

export default Article