/**
 * 车辆数据模型
 */

export interface Vehicle {
  id: string;           // 车辆ID，如"DM-01"
  type: string;         // 车辆类型："小型"/"中型"/"大型"
  status: string;       // 车辆状态："idle"/"busy"/"charge"/"fault"
  battery: number;      // 电量百分比 0-100
  loc: string;          // 当前位置，如"A区-3号点"
  task: string;         // 当前任务描述
  todayKm: number;      // 今日行驶里程(km)
  todayOrders: number;  // 今日订单数量
  energyPer100km: number; // 百公里能耗(kWh)
  totalKm: number;      // 总行驶里程(km)
}

// 车辆状态类型
export enum VehicleStatusType {
  IDLE = 'idle',
  BUSY = 'busy', 
  CHARGE = 'charge',
  FAULT = 'fault'
}

// 车辆类型
export enum VehicleType {
  SMALL = '小型',
  MEDIUM = '中型',
  LARGE = '大型'
}

export function createDefaultVehicles(): Vehicle[] {
  return [
    { id: 'DM-01', type: '小型', status: 'idle', battery: 87, loc: 'A区-3号点', task: '待命', todayKm: 42, todayOrders: 8, energyPer100km: 6.1, totalKm: 12847 },
    { id: 'DM-02', type: '小型', status: 'idle', battery: 92, loc: 'B区-1号点', task: '待命', todayKm: 38, todayOrders: 7, energyPer100km: 5.9, totalKm: 11234 },
    { id: 'DM-03', type: '中型', status: 'busy', battery: 64, loc: 'C区-途中配送#1842', task: '配送中', todayKm: 56, todayOrders: 6, energyPer100km: 8.7, totalKm: 15678 },
    { id: 'DM-04', type: '小型', status: 'idle', battery: 45, loc: 'A区-5号点', task: '待命', todayKm: 31, todayOrders: 5, energyPer100km: 6.4, totalKm: 9876 },
    { id: 'DM-05', type: '小型', status: 'busy', battery: 71, loc: 'D区-途中配送#1845', task: '配送中', todayKm: 47, todayOrders: 9, energyPer100km: 6.0, totalKm: 13456 },
    { id: 'DM-06', type: '中型', status: 'charge', battery: 12, loc: '充电站-2', task: '充电中', todayKm: 28, todayOrders: 4, energyPer100km: 8.9, totalKm: 16789 },
    { id: 'DM-07', type: '小型', status: 'idle', battery: 55, loc: 'B区-2号点', task: '待命', todayKm: 35, todayOrders: 6, energyPer100km: 6.3, totalKm: 10543 },
    { id: 'DM-08', type: '小型', status: 'idle', battery: 38, loc: 'A区-1号点', task: '待命', todayKm: 44, todayOrders: 8, energyPer100km: 5.8, totalKm: 8921 },
    { id: 'DM-09', type: '中型', status: 'busy', battery: 53, loc: 'E区-途中配送#1846', task: '配送中', todayKm: 51, todayOrders: 5, energyPer100km: 8.3, totalKm: 14567 },
    { id: 'DM-10', type: '中型', status: 'idle', battery: 29, loc: 'C区-4号点', task: '待命', todayKm: 22, todayOrders: 3, energyPer100km: 8.6, totalKm: 7890 },
    { id: 'DM-11', type: '大型', status: 'fault', battery: 41, loc: 'B区-3号点', task: '传感器异常', todayKm: 15, todayOrders: 2, energyPer100km: 12.1, totalKm: 18234 },
    { id: 'DM-12', type: '小型', status: 'idle', battery: 76, loc: 'D区-2号点', task: '待命', todayKm: 39, todayOrders: 7, energyPer100km: 6.0, totalKm: 12112 }
  ];
}

export function statusText(status: string): string {
  const map: Record<string, string> = {
    'idle': '待命',
    'busy': '配送中',
    'charge': '充电中',
    'fault': '故障'
  };
  return map[status] || status;
}

export function batteryColor(battery: number): string {
  if (battery > 50) return '#4ade80';
  if (battery > 20) return '#fbbf24';
  return '#f87171';
}

export function statusColor(status: string): string {
  const map: Record<string, string> = {
    'idle': '#4ade80',
    'busy': '#4a9eff',
    'charge': '#fbbf24',
    'fault': '#f87171'
  };
  return map[status] || '#64748b';
}