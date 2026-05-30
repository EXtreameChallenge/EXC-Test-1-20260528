package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.DispatchTask;
import com.claw.entity.FaultAlert;
import com.claw.entity.Vehicle;
import com.claw.mapper.DispatchTaskMapper;
import com.claw.mapper.FaultAlertMapper;
import com.claw.mapper.VehicleMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final VehicleMapper vehicleMapper;
    private final DispatchTaskMapper taskMapper;
    private final FaultAlertMapper alertMapper;

    public Map<String, Object> getDashboardData() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        long totalVehicles = this.vehicleMapper.selectCount(null);
        long standbyCount = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "standby"));
        long deliveringCount = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "delivering"));
        long chargingCount = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "charging"));
        long faultCount = this.vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "fault"));
        data.put("totalVehicles", totalVehicles);
        data.put("todayOrders", 1847);
        data.put("completionRate", 98.2);
        data.put("avgDeliveryTime", 23);
        data.put("totalEnergyConsumption", 89.5);
        LinkedHashMap<String, Long> statusDist = new LinkedHashMap<String, Long>();
        statusDist.put("standby", standbyCount);
        statusDist.put("delivering", deliveringCount);
        statusDist.put("charging", chargingCount);
        statusDist.put("fault", faultCount);
        data.put("vehicleStatusDistribution", statusDist);
        long pendingTasks = this.taskMapper.selectCount(new LambdaQueryWrapper<DispatchTask>().eq(DispatchTask::getStatus, "pending"));
        long executingTasks = this.taskMapper.selectCount(new LambdaQueryWrapper<DispatchTask>().eq(DispatchTask::getStatus, "executing"));
        long completedTasks = this.taskMapper.selectCount(new LambdaQueryWrapper<DispatchTask>().eq(DispatchTask::getStatus, "completed"));
        data.put("pendingTasks", pendingTasks);
        data.put("executingTasks", executingTasks);
        data.put("completedTasks", completedTasks);
        long unconfirmedAlerts = this.alertMapper.selectCount(new LambdaQueryWrapper<FaultAlert>().eq(FaultAlert::getStatus, "unconfirmed"));
        data.put("unconfirmedAlerts", unconfirmedAlerts);
        ArrayList<Map<String, Object>> hourlyOrders = new ArrayList<>();
        int[] hours = new int[]{6, 8, 10, 12, 14, 16, 18, 20, 22};
        int[] counts = new int[]{5, 18, 32, 45, 38, 28, 42, 22, 8};
        for (int i = 0; i < hours.length; ++i) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("hour", String.format("%02d:00", hours[i]));
            item.put("count", counts[i]);
            hourlyOrders.add(item);
        }
        data.put("hourlyOrders", hourlyOrders);
        ArrayList<Map<String, Object>> regionDistribution = new ArrayList<>();
        String[] regions = new String[]{"\u5f20\u6c5f", "\u6f15\u6cb3\u6cfe", "\u5357\u4eac\u897f\u8def", "\u4e94\u89d2\u573a", "\u8679\u6865"};
        int[] regionCounts = new int[]{45, 38, 32, 28, 22};
        for (int i = 0; i < regions.length; ++i) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("region", regions[i]);
            item.put("count", regionCounts[i]);
            regionDistribution.add(item);
        }
        data.put("regionDistribution", regionDistribution);
        ArrayList<Map<String, Object>> topVehicles = new ArrayList<>();
        String[] vehicleIds = new String[]{"DM-03", "DM-05", "DM-09", "DM-01"};
        int[] orders = new int[]{156, 142, 138, 125};
        int[] kms = new int[]{892, 756, 823, 645};
        for (int i = 0; i < vehicleIds.length; ++i) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("id", vehicleIds[i]);
            item.put("orders", orders[i]);
            item.put("km", kms[i]);
            topVehicles.add(item);
        }
        data.put("topVehicles", topVehicles);
        ArrayList<Map<String, Object>> recentTasks = new ArrayList<>();
        List<DispatchTask> tasks = this.taskMapper.selectList((new LambdaQueryWrapper<DispatchTask>().orderByDesc(DispatchTask::getCreatedAt)).last("LIMIT 5"));
        for (DispatchTask t : tasks) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("id", t.getId());
            item.put("name", t.getDestination() != null ? t.getDestination() : "Task-" + t.getId());
            item.put("destination", t.getDestination());
            item.put("status", t.getStatus());
            item.put("executeTime", t.getCreatedAt() != null ? t.getCreatedAt().toString() : "");
            recentTasks.add(item);
        }
        data.put("recentTasks", recentTasks);
        ArrayList<Map<String, Object>> aiInsights = new ArrayList<>();
        HashMap<String, Object> insight1 = new HashMap<String, Object>();
        insight1.put("id", 1);
        insight1.put("type", "success");
        insight1.put("title", "\u914d\u9001\u6548\u7387\u63d0\u5347");
        insight1.put("description", "\u672c\u5468\u5e73\u5747\u914d\u9001\u65f6\u6548\u63d0\u53478%\uff0c\u5efa\u8bae\u4fdd\u6301\u5f53\u524d\u8c03\u5ea6\u7b56\u7565");
        aiInsights.add(insight1);
        HashMap<String, Object> insight2 = new HashMap<String, Object>();
        insight2.put("id", 2);
        insight2.put("type", "warning");
        insight2.put("title", "\u7535\u91cf\u9884\u8b66");
        insight2.put("description", "DM-06\u7535\u91cf\u4ec5\u526912%\uff0c\u5efa\u8bae\u5c3d\u5feb\u5b89\u6392\u5145\u7535");
        aiInsights.add(insight2);
        HashMap<String, Object> insight3 = new HashMap<String, Object>();
        insight3.put("id", 3);
        insight3.put("type", "info");
        insight3.put("title", "\u9ad8\u5cf0\u9884\u6d4b");
        insight3.put("description", "\u9884\u8ba114:00-16:00\u4e3a\u914d\u9001\u9ad8\u5cf0\uff0c\u5efa\u8bae\u63d0\u524d\u8c03\u5ea6\u5907\u7528\u8f66\u8f86");
        aiInsights.add(insight3);
        data.put("aiInsights", aiInsights);
        return data;
    }

    public AnalyticsService(VehicleMapper vehicleMapper, DispatchTaskMapper taskMapper, FaultAlertMapper alertMapper) {
        this.vehicleMapper = vehicleMapper;
        this.taskMapper = taskMapper;
        this.alertMapper = alertMapper;
    }
}
