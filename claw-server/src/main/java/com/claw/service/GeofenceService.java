package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.Geofence;
import com.claw.entity.Vehicle;
import com.claw.mapper.GeofenceMapper;
import com.claw.mapper.VehicleMapper;
import com.claw.service.WebSocketService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GeofenceService {
    private final GeofenceMapper geofenceMapper;
    private final VehicleMapper vehicleMapper;
    private final WebSocketService webSocketService;

    public List<Geofence> listGeofences() {
        return this.geofenceMapper.selectList(new LambdaQueryWrapper<Geofence>().orderByDesc(Geofence::getCreatedAt));
    }

    public Geofence getGeofence(Long id) {
        return (Geofence)this.geofenceMapper.selectById(id);
    }

    public Geofence createGeofence(Geofence geofence) {
        if (geofence.getStatus() == null) {
            geofence.setStatus(1);
        }
        this.geofenceMapper.insert(geofence);
        return geofence;
    }

    public Geofence updateGeofence(Long id, Geofence geofence) {
        geofence.setId(id);
        this.geofenceMapper.updateById(geofence);
        return geofence;
    }

    public void deleteGeofence(Long id) {
        this.geofenceMapper.deleteById(id);
    }

    public List<Map<String, Object>> checkViolations() {
        List<Geofence> activeFences = this.geofenceMapper.selectList(new LambdaQueryWrapper<Geofence>().eq(Geofence::getStatus, (Object)1));
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        ArrayList<Map<String, Object>> violations = new ArrayList<Map<String, Object>>();
        for (Vehicle v : vehicles) {
            if (v.getLatitude() == null || v.getLongitude() == null) continue;
            for (Geofence f : activeFences) {
                LinkedHashMap<String, Object> violation;
                boolean inside = this.isPointInFence(v.getLatitude(), v.getLongitude(), f.getBoundary());
                if ("forbidden".equals(f.getType()) && inside) {
                    violation = new LinkedHashMap<String, Object>();
                    violation.put("vehicleId", v.getId());
                    violation.put("vehicleName", v.getName());
                    violation.put("geofenceId", f.getId());
                    violation.put("geofenceName", f.getName());
                    violation.put("type", "enter_forbidden");
                    violation.put("timestamp", System.currentTimeMillis());
                    violations.add(violation);
                    this.webSocketService.broadcastVehicleUpdate(v.getId(), "geofence_violation");
                }
                if (!"allowed".equals(f.getType()) || inside) continue;
                violation = new LinkedHashMap<>();
                violation.put("vehicleId", v.getId());
                violation.put("vehicleName", v.getName());
                violation.put("geofenceId", f.getId());
                violation.put("geofenceName", f.getName());
                violation.put("type", "leave_allowed");
                violation.put("timestamp", System.currentTimeMillis());
                violations.add(violation);
            }
        }
        return violations;
    }

    private boolean isPointInFence(BigDecimal lat, BigDecimal lng, String boundary) {
        if (boundary == null || boundary.isEmpty()) {
            return false;
        }
        try {
            String[] parts = boundary.split(";");
            if (parts.length == 2) {
                String[] center = parts[0].split(",");
                String radiusStr = parts[1];
                double cLat = Double.parseDouble(center[0]);
                double cLng = Double.parseDouble(center[1]);
                double radius = Double.parseDouble(radiusStr);
                double dist = this.haversine(lat.doubleValue(), lng.doubleValue(), cLat, cLng);
                return dist <= radius;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    private double haversine(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return R * c;
    }

    public GeofenceService(GeofenceMapper geofenceMapper, VehicleMapper vehicleMapper, WebSocketService webSocketService) {
        this.geofenceMapper = geofenceMapper;
        this.vehicleMapper = vehicleMapper;
        this.webSocketService = webSocketService;
    }
}
