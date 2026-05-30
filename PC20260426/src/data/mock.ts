export interface Vehicle {
  id: string
  name: string
  model: string
  status: 'standby' | 'delivery' | 'charging' | 'fault'
  battery: number
  mileage: number
  location: string
  lastUpdate: string
  driver?: string
}

export interface Task {
  id: string
  name: string
  status: 'pending' | 'confirmed' | 'executing' | 'completed' | 'cancelled'
  destination: string
  cargoType: string
  vehicleId?: string
  vehicleName?: string
  executeTime: string
  createdAt: string
}

export interface Alert {
  id: string
  level: 'critical' | 'warning' | 'info'
  message: string
  vehicleId?: string
  vehicleName?: string
  time: string
  status: 'unconfirmed' | 'confirmed' | 'ignored'
}

export interface WorkOrder {
  id: string
  title: string
  status: 'pending' | 'processing' | 'completed'
  priority: 'high' | 'medium' | 'low'
  assignee?: string
  createdAt: string
  completedAt?: string
}

export interface User {
  id: string
  username: string
  name: string
  email: string
  role: string
  status: 'active' | 'disabled'
  createdAt: string
}

export const vehicles: Vehicle[] = [
  { id: 'V001', name: '无人车-001', model: 'DM-A1', status: 'standby', battery: 87, mileage: 12580, location: 'A区充电站', lastUpdate: '2024-01-15 14:30', driver: '自动' },
  { id: 'V002', name: '无人车-002', model: 'DM-A1', status: 'delivery', battery: 62, mileage: 23410, location: 'B区配送点', lastUpdate: '2024-01-15 14:28', driver: '自动' },
  { id: 'V003', name: '无人车-003', model: 'DM-A2', status: 'charging', battery: 34, mileage: 18920, location: 'C区充电站', lastUpdate: '2024-01-15 14:25', driver: '自动' },
  { id: 'V004', name: '无人车-004', model: 'DM-A1', status: 'standby', battery: 91, mileage: 8760, location: 'D区待命点', lastUpdate: '2024-01-15 14:32', driver: '自动' },
  { id: 'V005', name: '无人车-005', model: 'DM-A2', status: 'fault', battery: 45, mileage: 32150, location: 'E区维修点', lastUpdate: '2024-01-15 14:20', driver: '自动' },
  { id: 'V006', name: '无人车-006', model: 'DM-A1', status: 'delivery', battery: 73, mileage: 15670, location: 'F区配送点', lastUpdate: '2024-01-15 14:29', driver: '自动' },
  { id: 'V007', name: '无人车-007', model: 'DM-A2', status: 'standby', battery: 95, mileage: 6780, location: 'G区待命点', lastUpdate: '2024-01-15 14:31', driver: '自动' },
  { id: 'V008', name: '无人车-008', model: 'DM-A1', status: 'charging', battery: 28, mileage: 28900, location: 'H区充电站', lastUpdate: '2024-01-15 14:22', driver: '自动' },
  { id: 'V009', name: '无人车-009', model: 'DM-A2', status: 'delivery', battery: 55, mileage: 19840, location: 'I区配送点', lastUpdate: '2024-01-15 14:27', driver: '自动' },
  { id: 'V010', name: '无人车-010', model: 'DM-A1', status: 'standby', battery: 88, mileage: 11230, location: 'J区待命点', lastUpdate: '2024-01-15 14:33', driver: '自动' },
  { id: 'V011', name: '无人车-011', model: 'DM-A2', status: 'delivery', battery: 41, mileage: 24560, location: 'K区配送点', lastUpdate: '2024-01-15 14:26', driver: '自动' },
  { id: 'V012', name: '无人车-012', model: 'DM-A1', status: 'charging', battery: 19, mileage: 36780, location: 'L区充电站', lastUpdate: '2024-01-15 14:21', driver: '自动' }
]

export const tasks: Task[] = [
  { id: 'T001', name: '配送任务-001', status: 'executing', destination: '科技园A区', cargoType: '快递包裹', vehicleId: 'V002', vehicleName: '无人车-002', executeTime: '2024-01-15 14:00', createdAt: '2024-01-15 10:00' },
  { id: 'T002', name: '配送任务-002', status: 'confirmed', destination: '商业中心B区', cargoType: '餐饮外卖', vehicleId: 'V006', vehicleName: '无人车-006', executeTime: '2024-01-15 15:00', createdAt: '2024-01-15 11:00' },
  { id: 'T003', name: '配送任务-003', status: 'pending', destination: '住宅区C区', cargoType: '生活用品', vehicleId: undefined, vehicleName: undefined, executeTime: '2024-01-15 16:00', createdAt: '2024-01-15 12:00' },
  { id: 'T004', name: '配送任务-004', status: 'completed', destination: '工业园D区', cargoType: '工业零件', vehicleId: 'V009', vehicleName: '无人车-009', executeTime: '2024-01-15 13:00', createdAt: '2024-01-15 09:00' },
  { id: 'T005', name: '配送任务-005', status: 'cancelled', destination: '学校E区', cargoType: '图书资料', vehicleId: undefined, vehicleName: undefined, executeTime: '2024-01-15 17:00', createdAt: '2024-01-15 08:00' },
  { id: 'T006', name: '配送任务-006', status: 'executing', destination: '医院F区', cargoType: '医疗用品', vehicleId: 'V011', vehicleName: '无人车-011', executeTime: '2024-01-15 14:30', createdAt: '2024-01-15 11:30' },
  { id: 'T007', name: '配送任务-007', status: 'pending', destination: '物流园G区', cargoType: '快递包裹', vehicleId: undefined, vehicleName: undefined, executeTime: '2024-01-15 18:00', createdAt: '2024-01-15 13:00' },
  { id: 'T008', name: '配送任务-008', status: 'completed', destination: '科技园H区', cargoType: '电子产品', vehicleId: 'V004', vehicleName: '无人车-004', executeTime: '2024-01-15 12:00', createdAt: '2024-01-15 08:30' }
]

export const alerts: Alert[] = [
  { id: 'A001', level: 'critical', message: '车辆V005发生故障，无法行驶', vehicleId: 'V005', vehicleName: '无人车-005', time: '2024-01-15 14:20', status: 'unconfirmed' },
  { id: 'A002', level: 'warning', message: '车辆V003电量低于20%，建议充电', vehicleId: 'V003', vehicleName: '无人车-003', time: '2024-01-15 14:15', status: 'unconfirmed' },
  { id: 'A003', level: 'info', message: '车辆V001已完成充电', vehicleId: 'V001', vehicleName: '无人车-001', time: '2024-01-15 14:10', status: 'confirmed' },
  { id: 'A004', level: 'warning', message: '车辆V008偏离预定路线', vehicleId: 'V008', vehicleName: '无人车-008', time: '2024-01-15 14:05', status: 'unconfirmed' },
  { id: 'A005', level: 'critical', message: '车辆V012电池温度过高', vehicleId: 'V012', vehicleName: '无人车-012', time: '2024-01-15 14:00', status: 'unconfirmed' },
  { id: 'A006', level: 'info', message: '车辆V007已到达待命点', vehicleId: 'V007', vehicleName: '无人车-007', time: '2024-01-15 13:55', status: 'confirmed' }
]

export const workOrders: WorkOrder[] = [
  { id: 'W001', title: 'V005故障维修', status: 'processing', priority: 'high', assignee: '张师傅', createdAt: '2024-01-15 14:25', completedAt: undefined },
  { id: 'W002', title: 'V012电池检查', status: 'pending', priority: 'high', assignee: undefined, createdAt: '2024-01-15 14:20', completedAt: undefined },
  { id: 'W003', title: 'V003常规保养', status: 'completed', priority: 'medium', assignee: '李师傅', createdAt: '2024-01-15 10:00', completedAt: '2024-01-15 12:00' },
  { id: 'W004', title: 'V008路线校准', status: 'pending', priority: 'low', assignee: undefined, createdAt: '2024-01-15 14:10', completedAt: undefined },
  { id: 'W005', title: 'V001系统升级', status: 'completed', priority: 'medium', assignee: '王师傅', createdAt: '2024-01-15 09:00', completedAt: '2024-01-15 11:30' }
]

export const users: User[] = [
  { id: 'U001', username: 'admin', name: '超级管理员', email: 'admin@demo.com', role: '超级管理员', status: 'active', createdAt: '2024-01-01' },
  { id: 'U002', username: 'operator1', name: '运营管理员1', email: 'op1@demo.com', role: '运营管理员', status: 'active', createdAt: '2024-01-05' },
  { id: 'U003', username: 'dispatcher1', name: '调度员1', email: 'dp1@demo.com', role: '调度员', status: 'active', createdAt: '2024-01-10' },
  { id: 'U004', username: 'monitor1', name: '监控员1', email: 'mn1@demo.com', role: '监控员', status: 'active', createdAt: '2024-01-12' },
  { id: 'U005', username: 'repair1', name: '维修员1', email: 'rp1@demo.com', role: '维修员', status: 'disabled', createdAt: '2024-01-15' }
]

export const dashboardStats = {
  totalVehicles: 12,
  todayOrders: 156,
  completionRate: 94.2,
  avgTime: 28,
  powerConsumption: 342.5
}

export const hourlyOrders = [
  { hour: '00:00', count: 12 },
  { hour: '02:00', count: 8 },
  { hour: '04:00', count: 5 },
  { hour: '06:00', count: 15 },
  { hour: '08:00', count: 45 },
  { hour: '10:00', count: 62 },
  { hour: '12:00', count: 78 },
  { hour: '14:00', count: 56 },
  { hour: '16:00', count: 48 },
  { hour: '18:00', count: 38 },
  { hour: '20:00', count: 25 },
  { hour: '22:00', count: 18 }
]

export const regionDistribution = [
  { region: 'A区', count: 35 },
  { region: 'B区', count: 28 },
  { region: 'C区', count: 22 },
  { region: 'D区', count: 18 },
  { region: 'E区', count: 15 },
  { region: 'F区', count: 12 },
  { region: 'G区', count: 10 },
  { region: 'H区', count: 8 }
]

export const vehicleStatusDistribution = [
  { status: '待命', count: 4, color: '#4ade80' },
  { status: '配送', count: 4, color: '#4a9eff' },
  { status: '充电', count: 3, color: '#fbbf24' },
  { status: '故障', count: 1, color: '#f87171' }
]

export const roles = [
  { id: 'superadmin', name: '超级管理员', permissions: ['all'] },
  { id: 'operator', name: '运营管理员', permissions: ['dashboard', 'fleet', 'dispatch', 'analytics'] },
  { id: 'dispatcher', name: '调度员', permissions: ['dashboard', 'fleet', 'dispatch'] },
  { id: 'monitor', name: '监控员', permissions: ['dashboard', 'fleet', 'faults'] },
  { id: 'repair', name: '维修员', permissions: ['faults'] }
]
