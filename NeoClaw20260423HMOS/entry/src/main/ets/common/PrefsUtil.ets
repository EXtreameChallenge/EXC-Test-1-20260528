/**
 * Preferences存储工具类
 */

import preferences from '@ohos.data.preferences';
import { StorageKeys } from './Constants';
import { Vehicle } from '../model/Vehicle';
import { LogEntry } from '../model/LogEntry';
import { ChatMessage } from '../model/ChatMessage';
import { UserInfo } from '../model/UserInfo';

class PrefsUtil {
  private static prefs: preferences.Preferences | null = null;

  // 初始化Preferences
  static async init(): Promise<void> {
    try {
      const context = getContext();
      PrefsUtil.prefs = await preferences.getPreferences(context, 'claw_prefs');
      console.log('Preferences初始化成功');
    } catch (error) {
      console.error('Preferences初始化失败:', error);
    }
  }

  // 存储字符串
  static async putString(key: string, value: string): Promise<boolean> {
    if (!PrefsUtil.prefs) {
      await PrefsUtil.init();
    }
    
    try {
      await PrefsUtil.prefs?.put(key, value);
      await PrefsUtil.prefs?.flush();
      return true;
    } catch (error) {
      console.error('存储字符串失败:', error);
      return false;
    }
  }

  // 获取字符串
  static async getString(key: string, defaultValue: string = ''): Promise<string> {
    if (!PrefsUtil.prefs) {
      await PrefsUtil.init();
    }
    
    try {
      const value = await PrefsUtil.prefs?.get(key, defaultValue);
      return value as string;
    } catch (error) {
      console.error('获取字符串失败:', error);
      return defaultValue;
    }
  }

  // 删除数据
  static async remove(key: string): Promise<boolean> {
    if (!PrefsUtil.prefs) {
      await PrefsUtil.init();
    }
    
    try {
      await PrefsUtil.prefs?.delete(key);
      await PrefsUtil.prefs?.flush();
      return true;
    } catch (error) {
      console.error('删除数据失败:', error);
      return false;
    }
  }

  // 清空所有数据
  static async clear(): Promise<boolean> {
    if (!PrefsUtil.prefs) {
      await PrefsUtil.init();
    }
    
    try {
      await PrefsUtil.prefs?.clear();
      await PrefsUtil.prefs?.flush();
      return true;
    } catch (error) {
      console.error('清空数据失败:', error);
      return false;
    }
  }

  // 存储对象（JSON序列化）
  static async putObject(key: string, value: Object): Promise<boolean> {
    try {
      const jsonString = JSON.stringify(value);
      return await PrefsUtil.putString(key, jsonString);
    } catch (error) {
      console.error('存储对象失败:', error);
      return false;
    }
  }

  // 获取对象（JSON反序列化）
  static async getObject<T>(key: string, defaultValue: T): Promise<T> {
    try {
      const jsonString = await PrefsUtil.getString(key, '');
      if (jsonString) {
        return JSON.parse(jsonString) as T;
      }
      return defaultValue;
    } catch (error) {
      console.error('获取对象失败:', error);
      return defaultValue;
    }
  }

  // 存储Token
  static async saveToken(token: string): Promise<boolean> {
    return await PrefsUtil.putString(StorageKeys.TOKEN, token);
  }

  // 获取Token
  static async getToken(): Promise<string> {
    return await PrefsUtil.getString(StorageKeys.TOKEN, '');
  }

  // 存储Refresh Token
  static async saveRefreshToken(refreshToken: string): Promise<boolean> {
    return await PrefsUtil.putString(StorageKeys.REFRESH_TOKEN, refreshToken);
  }

  // 获取Refresh Token
  static async getRefreshToken(): Promise<string> {
    return await PrefsUtil.getString(StorageKeys.REFRESH_TOKEN, '');
  }

  // 存储当前用户
  static async saveCurrentUser(user: UserInfo): Promise<boolean> {
    return await PrefsUtil.putObject(StorageKeys.CURRENT_USER, user);
  }

  // 获取当前用户
  static async getCurrentUser(): Promise<UserInfo | null> {
    return await PrefsUtil.getObject(StorageKeys.CURRENT_USER, null);
  }

  // 存储车辆数据
  static async saveVehicles(vehicles: Vehicle[]): Promise<boolean> {
    return await PrefsUtil.putObject(StorageKeys.VEHICLES, vehicles);
  }

  // 获取车辆数据
  static async getVehicles(): Promise<Vehicle[]> {
    return await PrefsUtil.getObject(StorageKeys.VEHICLES, []);
  }

  // 存储日志数据
  static async saveLogs(logs: LogEntry[]): Promise<boolean> {
    return await PrefsUtil.putObject(StorageKeys.LOGS, logs);
  }

  // 获取日志数据
  static async getLogs(): Promise<LogEntry[]> {
    return await PrefsUtil.getObject(StorageKeys.LOGS, []);
  }

  // 存储聊天历史
  static async saveChatHistory(history: ChatMessage[]): Promise<boolean> {
    return await PrefsUtil.putObject(StorageKeys.CHAT_HISTORY, history);
  }

  // 获取聊天历史
  static async getChatHistory(): Promise<ChatMessage[]> {
    return await PrefsUtil.getObject(StorageKeys.CHAT_HISTORY, []);
  }

  // 存储本地用户
  static async saveLocalUsers(users: UserInfo[]): Promise<boolean> {
    return await PrefsUtil.putObject(StorageKeys.LOCAL_USERS, users);
  }

  // 获取本地用户
  static async getLocalUsers(): Promise<UserInfo[]> {
    return await PrefsUtil.getObject(StorageKeys.LOCAL_USERS, []);
  }

  // 清除所有认证相关数据
  static async clearAuthData(): Promise<boolean> {
    try {
      await PrefsUtil.remove(StorageKeys.TOKEN);
      await PrefsUtil.remove(StorageKeys.REFRESH_TOKEN);
      await PrefsUtil.remove(StorageKeys.CURRENT_USER);
      return true;
    } catch (error) {
      console.error('清除认证数据失败:', error);
      return false;
    }
  }
}

export default PrefsUtil;