/**
 * 日志条目数据模型
 */

export interface LogEntry {
  time: string;   // 时间格式："HH:MM"
  text: string;   // 日志内容
  type: string;   // 日志类型："info"/"ok"/"warn"
}

// 日志类型
export enum LogType {
  INFO = 'info',
  OK = 'ok',
  WARN = 'warn'
}