/**
 * HTTP请求工具类
 */

import http from '@ohos.net.http';
import PrefsUtil from './PrefsUtil';
import { ApiConfig, StorageKeys } from './Constants';

class HttpUtil {
  private static instance: HttpUtil | null = null;
  private httpRequest: http.HttpRequest | null = null;

  // 单例模式
  static getInstance(): HttpUtil {
    if (!this.instance) {
      this.instance = new HttpUtil();
    }
    return this.instance;
  }

  // 初始化HTTP请求
  private async initHttpRequest(): Promise<void> {
    this.httpRequest = http.createHttp();
  }

  // 获取请求头（包含Token）
  private async getHeaders(): Promise<Object> {
    const token = await PrefsUtil.getToken();
    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    return headers;
  }

  // 通用请求方法
  private async request(
    method: http.RequestMethod,
    url: string,
    data?: Object
  ): Promise<any> {
    if (!this.httpRequest) {
      await this.initHttpRequest();
    }

    try {
      const options: http.HttpRequestOptions = {
        method: method,
        header: await this.getHeaders(),
        connectTimeout: ApiConfig.TIMEOUT,
        readTimeout: ApiConfig.TIMEOUT
      };

      if (data) {
        options.extraData = JSON.stringify(data);
      }

      const response = await this.httpRequest.request(url, options);
      
      // 处理401未授权
      if (response.responseCode === 401) {
        await PrefsUtil.clearAuthData();
        // 这里可以触发重新登录逻辑
        throw new Error('身份验证已过期，请重新登录');
      }

      if (response.responseCode >= 200 && response.responseCode < 300) {
        if (response.result) {
          return JSON.parse(response.result.toString());
        }
        return null;
      } else {
        throw new Error(`HTTP ${response.responseCode}: ${response.result}`);
      }
    } catch (error) {
      console.error('HTTP请求失败:', error);
      throw error;
    }
  }

  // GET请求
  async get(url: string): Promise<any> {
    return this.request(http.RequestMethod.GET, url);
  }

  // POST请求
  async post(url: string, data?: Object): Promise<any> {
    return this.request(http.RequestMethod.POST, url, data);
  }

  // PUT请求
  async put(url: string, data?: Object): Promise<any> {
    return this.request(http.RequestMethod.PUT, url, data);
  }

  // DELETE请求
  async delete(url: string): Promise<any> {
    return this.request(http.RequestMethod.DELETE, url);
  }

  // 认证相关API

  // 登录
  async login(username: string, password: string): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/auth/login`;
    const data = { username, password };
    return this.post(url, data);
  }

  // 注册
  async register(userData: any): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/auth/register`;
    return this.post(url, userData);
  }

  // 刷新Token
  async refreshToken(refreshToken: string): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/auth/refresh`;
    const data = { refreshToken };
    return this.post(url, data);
  }

  // 车辆相关API

  // 获取所有车辆
  async getAllVehicles(): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles/all`;
    return this.get(url);
  }

  // 获取车辆列表（带分页和筛选）
  async getVehicles(params?: {
    page?: number;
    size?: number;
    status?: string;
    type?: string;
  }): Promise<any> {
    let url = `${ApiConfig.BASE_URL}/vehicles`;
    if (params) {
      const queryParams = new URLSearchParams();
      Object.keys(params).forEach(key => {
        if (params[key] !== undefined) {
          queryParams.append(key, params[key].toString());
        }
      });
      url += `?${queryParams.toString()}`;
    }
    return this.get(url);
  }

  // 获取单个车辆信息
  async getVehicle(id: string): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles/${id}`;
    return this.get(url);
  }

  // 创建车辆
  async createVehicle(vehicleData: any): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles`;
    return this.post(url, vehicleData);
  }

  // 更新车辆信息
  async updateVehicle(id: string, vehicleData: any): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles/${id}`;
    return this.put(url, vehicleData);
  }

  // 更新车辆状态
  async updateVehicleStatus(id: string, status: string): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles/${id}/status`;
    return this.put(url, { status });
  }

  // 删除车辆
  async deleteVehicle(id: string): Promise<any> {
    const url = `${ApiConfig.BASE_URL}/vehicles/${id}`;
    return this.delete(url);
  }

  // AI聊天API
  async chatWithGLM(messages: Array<{role: string, content: string}>): Promise<any> {
    const url = '/api/chat'; // GLM代理接口
    const data = {
      model: ApiConfig.GLM_MODEL,
      messages: messages,
      temperature: ApiConfig.GLM_TEMPERATURE,
      top_p: ApiConfig.GLM_TOP_P,
      max_tokens: ApiConfig.GLM_MAX_TOKENS
    };
    return this.post(url, data);
  }

  // 健康检查
  async healthCheck(): Promise<any> {
    const url = '/api/health';
    return this.get(url);
  }

  // 销毁HTTP请求
  destroy(): void {
    if (this.httpRequest) {
      this.httpRequest.destroy();
      this.httpRequest = null;
    }
  }
}

export default HttpUtil;