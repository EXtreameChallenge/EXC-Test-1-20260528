const express = require('express');
const http = require('http');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// 模拟数据库（与移动端 demo.html 共用同一套数据）
const mockData = {
  vehicles: [
    { id:'DM-01', type:'小型', status:'idle', battery:87, loc:'A区-3号点', task:'待命', todayKm:42, todayOrders:8, energyPer100km:6.1, totalKm:12847 },
    { id:'DM-02', type:'小型', status:'idle', battery:92, loc:'B区-1号点', task:'待命', todayKm:38, todayOrders:7, energyPer100km:5.9, totalKm:11234 },
    { id:'DM-03', type:'中型', status:'busy', battery:64, loc:'C区-途中', task:'配送#1842', todayKm:56, todayOrders:6, energyPer100km:8.7, totalKm:15678 },
    { id:'DM-04', type:'小型', status:'idle', battery:45, loc:'A区-5号点', task:'待命', todayKm:31, todayOrders:5, energyPer100km:6.4, totalKm:9876 },
    { id:'DM-05', type:'小型', status:'busy', battery:71, loc:'D区-途中', task:'配送#1845', todayKm:47, todayOrders:9, energyPer100km:6.0, totalKm:13456 },
    { id:'DM-06', type:'中型', status:'charge', battery:12, loc:'充电站-2', task:'充电中', todayKm:28, todayOrders:4, energyPer100km:8.9, totalKm:16789 },
    { id:'DM-07', type:'小型', status:'idle', battery:55, loc:'B区-2号点', task:'待命', todayKm:35, todayOrders:6, energyPer100km:6.3, totalKm:10543 },
    { id:'DM-08', type:'小型', status:'idle', battery:38, loc:'A区-1号点', task:'待命', todayKm:44, todayOrders:8, energyPer100km:5.8, totalKm:8921 },
    { id:'DM-09', type:'中型', status:'busy', battery:53, loc:'E区-途中', task:'配送#1846', todayKm:51, todayOrders:5, energyPer100km:8.3, totalKm:14567 },
    { id:'DM-10', type:'中型', status:'idle', battery:29, loc:'C区-4号点', task:'待命', todayKm:22, todayOrders:3, energyPer100km:8.6, totalKm:7890 },
    { id:'DM-11', type:'大型', status:'fault', battery:41, loc:'B区-3号点', task:'传感器异常', todayKm:15, todayOrders:2, energyPer100km:12.1, totalKm:18234 },
    { id:'DM-12', type:'小型', status:'idle', battery:76, loc:'D区-2号点', task:'待命', todayKm:39, todayOrders:7, energyPer100km:6.0, totalKm:12112 },
  ],
  tasks: [],
  logs: [
    { time:'12:28', text:'DM-03 开始配送 #1842', type:'info' },
    { time:'12:25', text:'DM-06 进入充电站-2', type:'ok' },
    { time:'12:18', text:'DM-11 上报传感器异常', type:'warn' },
    { time:'12:10', text:'DM-05 开始配送 #1845', type:'info' },
    { time:'11:58', text:'DM-09 开始配送 #1846', type:'info' },
    { time:'11:42', text:'DM-02 完成配送 #1841', type:'ok' },
  ]
};

// 解析 JSON body
app.use(express.json());

// 静态文件服务 (demo.html)
app.use(express.static(path.join(__dirname)));

// ===== GLM-4-Flash API 代理 =====
app.post('/api/chat', async (req, res) => {
  try {
    const response = await fetch('https://open.bigmodel.cn/api/paas/v4/chat/completions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${process.env.GLM_API_KEY || ''}`
      },
      body: JSON.stringify(req.body)
    });
    const data = await response.json();
    res.json(data);
  } catch (err) {
    console.error('GLM API Error:', err.message);
    res.status(500).json({ error: err.message });
  }
});

// ===== Spring Boot 后端代理 =====
app.use('/api/v1', (req, res) => {
  const target = `http://localhost:8081${req.originalUrl}`;
  fetch(target, {
    method: req.method,
    headers: { ...req.headers },
    body: req.body ? JSON.stringify(req.body) : undefined
  }).then(async (r) => {
    const data = await r.json().catch(() => null);
    res.status(r.status).json(data || { code: 502, message: '后端无响应' });
  }).catch((err) => {
    console.log('Spring Boot 未启动:', err.message);
    res.status(502).json({ code: 502, message: 'Spring Boot 后端未启动' });
  });
});

// ===== Mock API (PC 端 Vue 项目专用) =====

// 获取车辆列表
app.get('/api/vehicles', (req, res) => {
  const { status, type, page = 0, size = 20 } = req.query;
  let vehicles = [...mockData.vehicles];
  
  if (status) vehicles = vehicles.filter(v => v.status === status);
  if (type) vehicles = vehicles.filter(v => v.type === type);
  
  const start = parseInt(page) * parseInt(size);
  const paginated = vehicles.slice(start, start + parseInt(size));
  
  res.json({
    code: 200,
    data: {
      content: paginated,
      totalElements: vehicles.length,
      totalPages: Math.ceil(vehicles.length / parseInt(size)),
      currentPage: parseInt(page)
    }
  });
});

// 获取车辆详情
app.get('/api/vehicles/:id', (req, res) => {
  const vehicle = mockData.vehicles.find(v => v.id === req.params.id);
  if (!vehicle) return res.status(404).json({ code: 404, message: '车辆不存在' });
  
  res.json({ code: 200, data: vehicle });
});

// 批量操作车辆
app.post('/api/vehicles/batch', (req, res) => {
  const { vehicleIds, action } = req.body;
  if (!vehicleIds || !action) return res.status(400).json({ code: 400, message: '参数不完整' });
  
  vehicleIds.forEach(id => {
    const v = mockData.vehicles.find(x => x.id === id);
    if (!v) return;
    if (action === 'charge') { v.status = 'charge'; v.task = '充电中'; }
    else if (action === 'dispatch') { v.status = 'busy'; v.task = '执行配送任务'; }
    else if (action === 'repair') { v.task = '提交维修工单'; }
  });
  
  res.json({ code: 200, message: `批量${action}已提交`, data: { batchId: `BATCH-${Date.now()}`, affectedVehicles: vehicleIds } });
});

// 获取运营数据
app.get('/api/analytics/daily', (req, res) => {
  const totalOrders = mockData.vehicles.reduce((a, v) => a + v.todayOrders, 0);
  const totalKm = mockData.vehicles.reduce((a, v) => a + v.todayKm, 0);
  
  res.json({
    code: 200,
    data: {
      date: new Date().toISOString().split('T')[0],
      totalOrders,
      totalKm,
      completionRate: 98.2,
      avgTime: 23,
      comparedToYesterday: '+12.3%',
      byZone: {
        'A区': { orders: 412, percentage: 22.3, avgTime: 19 },
        'B区': { orders: 378, percentage: 20.5, avgTime: 21 },
        'C区': { orders: 356, percentage: 19.3, avgTime: 25 },
        'D区': { orders: 341, percentage: 18.5, avgTime: 22 },
        'E区': { orders: 360, percentage: 19.5, avgTime: 28 }
      },
      weeklyTrend: [
        { date: '2026-04-21', orders: 1523 },
        { date: '2026-04-22', orders: 1689 },
        { date: '2026-04-23', orders: 1756 },
        { date: '2026-04-24', orders: totalOrders }
      ]
    }
  });
});

// 获取能耗数据
app.get('/api/energy/stats', (req, res) => {
  const totalKm = mockData.vehicles.reduce((a, v) => a + v.todayKm, 0);
  const avgE = (mockData.vehicles.reduce((a, v) => a + v.energyPer100km, 0) / mockData.vehicles.length).toFixed(1);
  
  res.json({
    code: 200,
    data: {
      todayOrders: mockData.vehicles.reduce((a, v) => a + v.todayOrders, 0),
      todayKm: totalKm,
      completionRate: 98.2,
      avgTime: 23,
      energyConsumed: (totalKm * avgE / 100).toFixed(1),
      avgEnergyPer100km: parseFloat(avgE),
      energyRating: '良好',
      comparedToLastMonth: '-3.2%',
      byVehicleType: {
        '小型': { count: 7, avgEnergy: 6.2, totalEnergy: 28.4 },
        '中型': { count: 4, avgEnergy: 8.5, totalEnergy: 15.8 },
        '大型': { count: 1, avgEnergy: 12.1, totalEnergy: 3.4 }
      }
    }
  });
});

// 获取任务列表
app.get('/api/dispatch', (req, res) => {
  res.json({ code: 200, data: { content: mockData.tasks, totalElements: mockData.tasks.length } });
});

// 创建调度任务
app.post('/api/dispatch', (req, res) => {
  const task = { ...req.body, id: `TASK-${Date.now()}`, status: 'pending', createdAt: new Date().toISOString() };
  mockData.tasks.push(task);
  res.json({ code: 200, message: '调度任务已创建', data: task });
});

// 健康检查
app.get('/api/health', (req, res) => {
  res.json({
    status: 'ok',
    demo: '运行中',
    springBoot: 'pending',
    timestamp: new Date().toISOString()
  });
});

// SPA 回退
app.get('*', (req, res, next) => {
  if (req.path.startsWith('/api')) return next();
  res.sendFile(path.join(__dirname, 'demo.html'));
});

const server = http.createServer(app);
server.listen(PORT, () => {
  console.log(`德莫代理服务器运行在 http://localhost:${PORT}`);
  console.log(`移动端 Demo: http://localhost:${PORT}`);
  console.log(`PC 端管理端: http://localhost:5173 (请单独启动)`);
  console.log(`GLM API 代理: http://localhost:${PORT}/api/chat`);
  console.log(`Mock API: http://localhost:${PORT}/api/vehicles`);
});
