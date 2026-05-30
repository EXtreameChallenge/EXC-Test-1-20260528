/**
 * WebSocket管理类
 */

import websocket from '@ohos.net.websocket';
import PrefsUtil from './PrefsUtil';
import { WebSocketConfig, StorageKeys } from './Constants';

type MessageCallback = (data: any) => void;

export class WsManager {
  private static instance: WsManager | null = null;
  private ws: websocket.WebSocket | null = null;
  private reconnectTimer: number | null = null;
  private listeners: Map<string, MessageCallback[]> = new Map();
  private isConnected: boolean = false;
  private heartbeatTimer: number | null = null;

  // 单例模式
  static getInstance(): WsManager {
    if (!this.instance) {
      this.instance = new WsManager();
    }
    return this.instance;
  }

  // 连接WebSocket
  async connect(): Promise<void> {
    if (this.isConnected) {
      return;
    }

    try {
      const token = await PrefsUtil.getToken();
      if (!token) {
        console.warn('未找到Token，无法连接WebSocket');
        return;
      }

      // 创建WebSocket连接
      this.ws = new websocket.WebSocket();
      
      // 设置事件监听器
      this.ws.on('open', () => {
        console.log('WebSocket连接已建立');
        this.isConnected = true;
        this.sendConnectFrame(token);
        this.startHeartbeat();
        this.clearReconnectTimer();
      });

      this.ws.on('message', (data: websocket.MessageEvent) => {
        this.handleMessage(data);
      });

      this.ws.on('close', () => {
        console.log('WebSocket连接已关闭');
        this.isConnected = false;
        this.stopHeartbeat();
        this.scheduleReconnect();
      });

      this.ws.on('error', (error: Error) => {
        console.error('WebSocket错误:', error);
        this.isConnected = false;
        this.stopHeartbeat();
        this.scheduleReconnect();
      });

      // 连接WebSocket
      await this.ws.connect(WebSocketConfig.WS_URL);
    } catch (error) {
      console.error('WebSocket连接失败:', error);
      this.scheduleReconnect();
    }
  }

  // 发送STOMP CONNECT帧
  private sendConnectFrame(token: string): void {
    const connectFrame = {
      command: 'CONNECT',
      headers: {
        'accept-version': '1.2',
        'heart-beat': '10000,10000',
        'Authorization': `Bearer ${token}`
      },
      body: ''
    };
    
    this.sendStompFrame(connectFrame);
  }

  // 发送STOMP SUBSCRIBE帧
  subscribe(topic: string, callback: MessageCallback): void {
    // 存储回调函数
    if (!this.listeners.has(topic)) {
      this.listeners.set(topic, []);
    }
    this.listeners.get(topic)?.push(callback);

    // 发送订阅帧
    if (this.isConnected) {
      const subscribeFrame = {
        command: 'SUBSCRIBE',
        headers: {
          'id': `sub-${Date.now()}`,
          'destination': topic
        },
        body: ''
      };
      
      this.sendStompFrame(subscribeFrame);
    }
  }

  // 取消订阅
  unsubscribe(topic: string, callback?: MessageCallback): void {
    if (callback) {
      const callbacks = this.listeners.get(topic);
      if (callbacks) {
        const index = callbacks.indexOf(callback);
        if (index > -1) {
          callbacks.splice(index, 1);
        }
      }
    } else {
      this.listeners.delete(topic);
    }
  }

  // 发送消息
  send(destination: string, body: any): void {
    if (this.isConnected) {
      const sendFrame = {
        command: 'SEND',
        headers: {
          'destination': destination
        },
        body: JSON.stringify(body)
      };
      
      this.sendStompFrame(sendFrame);
    }
  }

  // 发送STOMP帧
  private sendStompFrame(frame: any): void {
    if (this.ws && this.isConnected) {
      const frameString = this.buildStompFrame(frame);
      this.ws.send(frameString);
    }
  }

  // 构建STOMP帧
  private buildStompFrame(frame: any): string {
    let frameString = `${frame.command}\n`;
    
    // 添加头部
    for (const [key, value] of Object.entries(frame.headers || {})) {
      frameString += `${key}:${value}\n`;
    }
    
    // 添加空行分隔头部和正文
    frameString += '\n';
    
    // 添加正文
    if (frame.body) {
      frameString += frame.body;
    }
    
    // 添加空字符结束帧
    frameString += '\0';
    
    return frameString;
  }

  // 处理接收到的消息
  private handleMessage(event: websocket.MessageEvent): void {
    try {
      const message = event.data as string;
      
      // 解析STOMP帧
      const frame = this.parseStompFrame(message);
      if (!frame) return;

      // 处理MESSAGE帧
      if (frame.command === 'MESSAGE') {
        const destination = frame.headers['destination'];
        const body = frame.body ? JSON.parse(frame.body) : null;
        
        // 触发对应主题的监听器
        const callbacks = this.listeners.get(destination);
        if (callbacks) {
          callbacks.forEach(callback => {
            try {
              callback(body);
            } catch (error) {
              console.error('WebSocket回调执行失败:', error);
            }
          });
        }
      }
    } catch (error) {
      console.error('处理WebSocket消息失败:', error);
    }
  }

  // 解析STOMP帧
  private parseStompFrame(message: string): any {
    const lines = message.split('\n');
    if (lines.length < 1) return null;

    const frame = {
      command: lines[0],
      headers: {},
      body: ''
    };

    let i = 1;
    // 解析头部
    while (i < lines.length && lines[i] !== '') {
      const [key, value] = lines[i].split(':');
      if (key && value !== undefined) {
        frame.headers[key] = value;
      }
      i++;
    }

    // 跳过空行
    i++;

    // 解析正文
    const bodyLines = [];
    while (i < lines.length && lines[i] !== '\0') {
      bodyLines.push(lines[i]);
      i++;
    }

    frame.body = bodyLines.join('\n');
    return frame;
  }

  // 启动心跳
  private startHeartbeat(): void {
    this.stopHeartbeat();
    
    this.heartbeatTimer = setInterval(() => {
      if (this.isConnected) {
        // 发送心跳帧
        this.sendStompFrame({
          command: '\n',
          headers: {},
          body: ''
        });
      }
    }, WebSocketConfig.HEARTBEAT_INTERVAL) as unknown as number;
  }

  // 停止心跳
  private stopHeartbeat(): void {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }

  // 安排重连
  private scheduleReconnect(): void {
    this.clearReconnectTimer();
    
    this.reconnectTimer = setTimeout(() => {
      this.connect();
    }, WebSocketConfig.RECONNECT_DELAY) as unknown as number;
  }

  // 清除重连定时器
  private clearReconnectTimer(): void {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
  }

  // 断开连接
  disconnect(): void {
    this.isConnected = false;
    this.stopHeartbeat();
    this.clearReconnectTimer();
    this.listeners.clear();
    
    if (this.ws) {
      // 发送DISCONNECT帧
      this.sendStompFrame({
        command: 'DISCONNECT',
        headers: {},
        body: ''
      });
      
      this.ws.close();
      this.ws = null;
    }
  }

  // 获取连接状态
  getConnectionStatus(): boolean {
    return this.isConnected;
  }
}

export default WsManager;