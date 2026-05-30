package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.DispatchTask;
import com.claw.entity.Vehicle;
import com.claw.mapper.DispatchTaskMapper;
import com.claw.mapper.VehicleMapper;
import com.claw.service.WebSocketService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DispatchPlanService {
    private final VehicleMapper vehicleMapper;
    private final DispatchTaskMapper taskMapper;
    private final WebSocketService webSocketService;

    public Map<String, Object> optimize(String targetArea, int vehicleCount, String priority, Long userId) {
        List<Vehicle> standbyVehicles = this.vehicleMapper.selectList(new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, "standby").ge(Vehicle::getBattery, new BigDecimal("20")).orderByDesc(Vehicle::getBattery).last("LIMIT " + Math.min(Math.max(vehicleCount, 1), 100)));
        ArrayList<Map<String, Object>> selectedVehicles = new ArrayList<>();
        for (Vehicle v : standbyVehicles) {
            LinkedHashMap<String, Object> vMap = new LinkedHashMap<String, Object>();
            vMap.put("vehicleId", v.getId());
            vMap.put("name", v.getName());
            vMap.put("battery", v.getBattery());
            vMap.put("location", v.getLocation());
            vMap.put("mileage", v.getMileage());
            selectedVehicles.add(vMap);
        }
        double optimizationScore = this.calculateOptimizationScore(standbyVehicles, vehicleCount);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("planId", "DP-" + System.currentTimeMillis());
        result.put("targetArea", targetArea);
        result.put("priority", priority != null ? priority : "medium");
        result.put("vehicles", selectedVehicles);
        result.put("vehicleCount", selectedVehicles.size());
        result.put("optimizationScore", optimizationScore);
        result.put("estimatedDuration", this.estimateDuration(targetArea, selectedVehicles.size()));
        result.put("createdAt", new Date());
        return result;
    }

    @Transactional
    public List<DispatchTask> executePlan(Map<String, Object> plan, Long userId) {
        String planId = (String)plan.get("planId");
        String targetArea = (String)plan.get("targetArea");
        String priority = (String)plan.getOrDefault("priority", "medium");
        @SuppressWarnings("unchecked") List<Map<String, Object>> vehicles = (List<Map<String, Object>>)plan.get("vehicles");
        ArrayList<DispatchTask> createdTasks = new ArrayList<DispatchTask>();
        for (Map<String, Object> vMap : vehicles) {
            String vehicleId = (String)vMap.get("vehicleId");
            DispatchTask task = new DispatchTask();
            task.setId("T-" + planId + "-" + vehicleId);
            task.setName(targetArea + " \u534f\u540c\u914d\u9001");
            task.setStatus("confirmed");
            task.setDestination(targetArea);
            task.setVehicleId(vehicleId);
            task.setPriority(priority);
            task.setCreatorId(userId);
            this.taskMapper.insert(task);
            Vehicle vehicle = (Vehicle)this.vehicleMapper.selectById((Serializable)(vehicleId));
            if (vehicle != null && "standby".equals(vehicle.getStatus())) {
                vehicle.setStatus("delivering");
                this.vehicleMapper.updateById(vehicle);
                this.webSocketService.broadcastVehicleUpdate(vehicleId, "delivering");
            }
            createdTasks.add(task);
        }
        return createdTasks;
    }

    private double calculateOptimizationScore(List<Vehicle> vehicles, int requestedCount) {
        if (vehicles.isEmpty()) {
            return 0.0;
        }
        double avgBattery = vehicles.stream().mapToDouble(v -> v.getBattery().doubleValue()).average().orElse(0.0);
        double coverageRatio = (double)vehicles.size() / (double)requestedCount;
        return Math.min(1.0, avgBattery / 100.0 * 0.6 + coverageRatio * 0.4);
    }

    private int estimateDuration(String area, int vehicleCount) {
        return Math.max(15, 60 / Math.max(1, vehicleCount));
    }

    public DispatchPlanService(VehicleMapper vehicleMapper, DispatchTaskMapper taskMapper, WebSocketService webSocketService) {
        this.vehicleMapper = vehicleMapper;
        this.taskMapper = taskMapper;
        this.webSocketService = webSocketService;
    }
}
