package com.claw.schedule;

import com.claw.entity.FaultAlert;
import com.claw.entity.Vehicle;
import com.claw.mapper.FaultAlertMapper;
import com.claw.mapper.VehicleMapper;
import com.claw.service.GeofenceService;
import com.claw.service.MaintenancePredictionService;
import com.claw.service.WebSocketService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final VehicleMapper vehicleMapper;
    private final FaultAlertMapper alertMapper;
    private final WebSocketService webSocketService;
    private final GeofenceService geofenceService;
    private final MaintenancePredictionService predictionService;

    @Scheduled(fixedRate=300000L)
    public void checkLowBatteryVehicles() {
        log.info("\u5b9a\u65f6\u4efb\u52a1: \u68c0\u67e5\u4f4e\u7535\u91cf\u8f66\u8f86");
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        for (Vehicle v : vehicles) {
            if (v.getBattery() == null || v.getBattery().compareTo(new BigDecimal("15")) >= 0 || "charging".equals(v.getStatus())) continue;
            FaultAlert alert = new FaultAlert();
            alert.setId("A-BAT-" + System.currentTimeMillis() + "-" + v.getId());
            alert.setLevel("warning");
            alert.setMessage(String.format("%s \u7535\u91cf\u4e25\u91cd\u4e0d\u8db3(%.0f%%)\uff0c\u5efa\u8bae\u7acb\u5373\u5145\u7535", v.getName(), v.getBattery().doubleValue()));
            alert.setVehicleId(v.getId());
            alert.setStatus("unconfirmed");
            this.alertMapper.insert(alert);
            this.webSocketService.pushNewAlert(1L, Map.of("alertId", alert.getId(), "level", "warning", "message", alert.getMessage(), "vehicleId", v.getId(), "timestamp", System.currentTimeMillis()));
        }
    }

    @Scheduled(fixedRate=600000L)
    public void checkGeofenceViolations() {
        log.info("\u5b9a\u65f6\u4efb\u52a1: \u68c0\u67e5\u56f4\u680f\u8fdd\u89c4");
        try {
            List<Map<String, Object>> violations = this.geofenceService.checkViolations();
            if (!violations.isEmpty()) {
                for (Map<String, Object> v : violations) {
                    this.webSocketService.pushNewAlert(1L, Map.of("type", "geofence_violation", "vehicleId", v.get("vehicleId"), "vehicleName", v.get("vehicleName"), "geofenceName", v.get("geofenceName"), "violationType", v.get("type"), "timestamp", System.currentTimeMillis()));
                }
            }
        }
        catch (Exception e) {
            log.error("\u56f4\u680f\u8fdd\u89c4\u68c0\u67e5\u5931\u8d25", (Throwable)e);
        }
    }

    @Scheduled(cron="0 0 2 * * ?")
    public void refreshMaintenancePredictions() {
        log.info("\u5b9a\u65f6\u4efb\u52a1: \u5237\u65b0\u7ef4\u62a4\u9884\u6d4b\u6570\u636e");
        try {
            this.predictionService.refreshPredictions();
        }
        catch (Exception e) {
            log.error("\u7ef4\u62a4\u9884\u6d4b\u5237\u65b0\u5931\u8d25", (Throwable)e);
        }
    }

    @Scheduled(fixedRate=300000L)
    public void checkStaleVehicles() {
        log.info("\u5b9a\u65f6\u4efb\u52a1: \u68c0\u67e5\u957f\u65f6\u95f4\u65e0\u66f4\u65b0\u8f66\u8f86");
        LocalDateTime threshold = LocalDateTime.now().minusHours(2L);
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        for (Vehicle v : vehicles) {
            if (v.getLastUpdate() == null || !v.getLastUpdate().isBefore(threshold) || "standby".equals(v.getStatus())) continue;
            this.webSocketService.pushNewAlert(1L, Map.of("type", "stale_vehicle", "vehicleId", v.getId(), "vehicleName", v.getName(), "lastUpdate", v.getLastUpdate().toString(), "message", v.getName() + " \u5df2\u8d85\u8fc72\u5c0f\u65f6\u65e0\u6570\u636e\u66f4\u65b0", "timestamp", System.currentTimeMillis()));
        }
    }

    public ScheduledTasks(VehicleMapper vehicleMapper, FaultAlertMapper alertMapper, WebSocketService webSocketService, GeofenceService geofenceService, MaintenancePredictionService predictionService) {
        this.vehicleMapper = vehicleMapper;
        this.alertMapper = alertMapper;
        this.webSocketService = webSocketService;
        this.geofenceService = geofenceService;
        this.predictionService = predictionService;
    }
}
