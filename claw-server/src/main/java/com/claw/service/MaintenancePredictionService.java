package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.MaintenancePrediction;
import com.claw.entity.Vehicle;
import com.claw.mapper.MaintenancePredictionMapper;
import com.claw.mapper.VehicleMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenancePredictionService {
    private final MaintenancePredictionMapper predictionMapper;
    private final VehicleMapper vehicleMapper;

    public List<MaintenancePrediction> listPredictions(String status, String vehicleId) {
        LambdaQueryWrapper<MaintenancePrediction> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MaintenancePrediction::getStatus, status);
        }
        if (vehicleId != null && !vehicleId.isEmpty()) {
            wrapper.eq(MaintenancePrediction::getVehicleId, vehicleId);
        }
        wrapper.orderByAsc(MaintenancePrediction::getHealthScore);
        return this.predictionMapper.selectList(wrapper);
    }

    public MaintenancePrediction getPrediction(Long id) {
        return this.predictionMapper.selectById(id);
    }

    public List<MaintenancePrediction> getVehiclePredictions(String vehicleId) {
        return this.predictionMapper.selectList(new LambdaQueryWrapper<MaintenancePrediction>()
                .eq(MaintenancePrediction::getVehicleId, vehicleId)
                .orderByAsc(MaintenancePrediction::getHealthScore));
    }

    public Map<String, Object> getOverview() {
        List<MaintenancePrediction> all = this.predictionMapper.selectList(null);
        long criticalCount = all.stream().filter(p -> "critical".equals(p.getStatus())).count();
        long warningCount = all.stream().filter(p -> "warning".equals(p.getStatus())).count();
        long normalCount = all.stream().filter(p -> "normal".equals(p.getStatus())).count();
        double avgHealth = all.stream().mapToDouble(p -> p.getHealthScore().doubleValue()).average().orElse(100.0);
        List<Map<String, Object>> urgentItems = all.stream()
                .filter(p -> "critical".equals(p.getStatus()) || "warning".equals(p.getStatus()))
                .sorted(Comparator.comparing(MaintenancePrediction::getHealthScore))
                .limit(10L)
                .map(p -> {
                    LinkedHashMap<String, Object> item = new LinkedHashMap<>();
                    item.put("id", p.getId());
                    item.put("vehicleId", p.getVehicleId());
                    item.put("component", p.getComponent());
                    item.put("healthScore", p.getHealthScore());
                    item.put("predictedFailureDate", p.getPredictedFailureDate());
                    item.put("recommendation", p.getRecommendation());
                    item.put("status", p.getStatus());
                    return item;
                }).collect(Collectors.toList());
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("totalPredictions", all.size());
        result.put("criticalCount", criticalCount);
        result.put("warningCount", warningCount);
        result.put("normalCount", normalCount);
        result.put("avgHealthScore", Math.round(avgHealth * 10.0) / 10.0);
        result.put("urgentItems", urgentItems);
        return result;
    }

    @Transactional
    public void refreshPredictions() {
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        for (Vehicle v : vehicles) {
            this.refreshVehiclePredictions(v);
        }
    }

    private void refreshVehiclePredictions(Vehicle vehicle) {
        String vehicleId = vehicle.getId();
        List<MaintenancePrediction> existing = this.predictionMapper.selectList(
                new LambdaQueryWrapper<MaintenancePrediction>().eq(MaintenancePrediction::getVehicleId, vehicleId));
        Map<String, MaintenancePrediction> existingMap = existing.stream()
                .collect(Collectors.toMap(MaintenancePrediction::getComponent, p -> p, (a, b) -> a));
        this.updateOrCreatePrediction(existingMap, vehicleId, "battery", this.calculateBatteryHealth(vehicle), this.calculateBatteryFailureDate(vehicle), this.generateBatteryRecommendation(vehicle));
        this.updateOrCreatePrediction(existingMap, vehicleId, "motor", this.calculateMotorHealth(vehicle), this.calculateMotorFailureDate(vehicle), this.generateMotorRecommendation(vehicle));
        this.updateOrCreatePrediction(existingMap, vehicleId, "sensor", this.calculateSensorHealth(vehicle), this.calculateSensorFailureDate(vehicle), this.generateSensorRecommendation(vehicle));
        this.updateOrCreatePrediction(existingMap, vehicleId, "tire", this.calculateTireHealth(vehicle), this.calculateTireFailureDate(vehicle), this.generateTireRecommendation(vehicle));
    }

    private void updateOrCreatePrediction(Map<String, MaintenancePrediction> existingMap, String vehicleId, String component, BigDecimal healthScore, LocalDate failureDate, String recommendation) {
        String status = healthScore.compareTo(new BigDecimal("40")) < 0 ? "critical" : (healthScore.compareTo(new BigDecimal("65")) < 0 ? "warning" : "normal");
        MaintenancePrediction existing = existingMap.get(component);
        if (existing != null) {
            existing.setHealthScore(healthScore);
            existing.setPredictedFailureDate(failureDate);
            existing.setRecommendation(recommendation);
            existing.setStatus(status);
            existing.setUpdatedAt(LocalDateTime.now());
            this.predictionMapper.updateById(existing);
        } else {
            MaintenancePrediction prediction = new MaintenancePrediction();
            prediction.setVehicleId(vehicleId);
            prediction.setComponent(component);
            prediction.setHealthScore(healthScore);
            prediction.setPredictedFailureDate(failureDate);
            prediction.setRecommendation(recommendation);
            prediction.setStatus(status);
            this.predictionMapper.insert(prediction);
        }
    }

    private BigDecimal calculateBatteryHealth(Vehicle v) {
        if (v.getBattery() == null) {
            return new BigDecimal("80");
        }
        double health = 100.0 - (v.getMileage() != null ? v.getMileage().doubleValue() * 0.3 : 0.0);
        if ("fault".equals(v.getStatus())) {
            health -= 30.0;
        }
        if ("charging".equals(v.getStatus())) {
            health -= 5.0;
        }
        return BigDecimal.valueOf(Math.max(10.0, Math.min(100.0, health)));
    }

    private BigDecimal calculateMotorHealth(Vehicle v) {
        double health = 95.0 - (v.getMileage() != null ? v.getMileage().doubleValue() * 0.2 : 0.0);
        if ("fault".equals(v.getStatus())) {
            health -= 25.0;
        }
        return BigDecimal.valueOf(Math.max(15.0, Math.min(100.0, health)));
    }

    private BigDecimal calculateSensorHealth(Vehicle v) {
        double health = 90.0;
        if ("fault".equals(v.getStatus())) {
            health -= 40.0;
        }
        if (v.getMileage() != null && v.getMileage().doubleValue() > 40.0) {
            health -= 10.0;
        }
        return BigDecimal.valueOf(Math.max(10.0, Math.min(100.0, health)));
    }

    private BigDecimal calculateTireHealth(Vehicle v) {
        double health = 92.0 - (v.getMileage() != null ? v.getMileage().doubleValue() * 0.15 : 0.0);
        return BigDecimal.valueOf(Math.max(20.0, Math.min(100.0, health)));
    }

    private LocalDate calculateBatteryFailureDate(Vehicle v) {
        BigDecimal health = this.calculateBatteryHealth(v);
        int daysOffset = health.intValue() > 60 ? 60 : (health.intValue() > 30 ? 14 : 3);
        return LocalDate.now().plusDays(daysOffset);
    }

    private LocalDate calculateMotorFailureDate(Vehicle v) {
        BigDecimal health = this.calculateMotorHealth(v);
        int daysOffset = health.intValue() > 60 ? 90 : (health.intValue() > 30 ? 30 : 7);
        return LocalDate.now().plusDays(daysOffset);
    }

    private LocalDate calculateSensorFailureDate(Vehicle v) {
        BigDecimal health = this.calculateSensorHealth(v);
        int daysOffset = health.intValue() > 60 ? 45 : (health.intValue() > 30 ? 10 : 3);
        return LocalDate.now().plusDays(daysOffset);
    }

    private LocalDate calculateTireFailureDate(Vehicle v) {
        BigDecimal health = this.calculateTireHealth(v);
        int daysOffset = health.intValue() > 60 ? 90 : (health.intValue() > 30 ? 30 : 10);
        return LocalDate.now().plusDays(daysOffset);
    }

    private String generateBatteryRecommendation(Vehicle v) {
        BigDecimal health = this.calculateBatteryHealth(v);
        if (health.compareTo(new BigDecimal("40")) < 0) {
            return "\u7535\u6c60\u4e25\u91cd\u8870\u51cf\uff0c\u5efa\u8bae\u7acb\u5373\u66f4\u6362";
        }
        if (health.compareTo(new BigDecimal("65")) < 0) {
            return "\u7535\u6c60\u8870\u51cf\u660e\u663e\uff0c\u5efa\u8bae\u5b89\u6392\u66f4\u6362";
        }
        return "\u7535\u6c60\u72b6\u6001\u6b63\u5e38\uff0c\u7ee7\u7eed\u76d1\u6d4b";
    }

    private String generateMotorRecommendation(Vehicle v) {
        BigDecimal health = this.calculateMotorHealth(v);
        if (health.compareTo(new BigDecimal("40")) < 0) {
            return "\u7535\u673a\u5f02\u5e38\uff0c\u5efa\u8bae\u7acb\u5373\u68c0\u4fee";
        }
        if (health.compareTo(new BigDecimal("65")) < 0) {
            return "\u7535\u673a\u8fd0\u884c\u4e0d\u7a33\u5b9a\uff0c\u5efa\u8bae\u68c0\u67e5";
        }
        return "\u7535\u673a\u8fd0\u884c\u6b63\u5e38\uff0c\u5b9a\u671f\u4fdd\u517b";
    }

    private String generateSensorRecommendation(Vehicle v) {
        BigDecimal health = this.calculateSensorHealth(v);
        if (health.compareTo(new BigDecimal("40")) < 0) {
            return "\u4f20\u611f\u5668\u6545\u969c\uff0c\u9700\u7acb\u5373\u7ef4\u4fee";
        }
        if (health.compareTo(new BigDecimal("65")) < 0) {
            return "\u4f20\u611f\u5668\u7cbe\u5ea6\u4e0b\u964d\uff0c\u5efa\u8bae\u6821\u51c6";
        }
        return "\u4f20\u611f\u5668\u5de5\u4f5c\u6b63\u5e38";
    }

    private String generateTireRecommendation(Vehicle v) {
        BigDecimal health = this.calculateTireHealth(v);
        if (health.compareTo(new BigDecimal("40")) < 0) {
            return "\u8f6e\u80ce\u4e25\u91cd\u78e8\u635f\uff0c\u5efa\u8bae\u66f4\u6362";
        }
        if (health.compareTo(new BigDecimal("65")) < 0) {
            return "\u8f6e\u80ce\u78e8\u635f\uff0c\u5efa\u8bae\u68c0\u67e5";
        }
        return "\u8f6e\u80ce\u72b6\u6001\u826f\u597d";
    }

    public MaintenancePredictionService(MaintenancePredictionMapper predictionMapper, VehicleMapper vehicleMapper) {
        this.predictionMapper = predictionMapper;
        this.vehicleMapper = vehicleMapper;
    }
}
