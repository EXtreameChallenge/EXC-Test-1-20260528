package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.Vehicle;
import com.claw.entity.VehicleTrack;
import com.claw.mapper.VehicleMapper;
import com.claw.mapper.VehicleTrackMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DigitalTwinService {
    private final VehicleMapper vehicleMapper;
    private final VehicleTrackMapper trackMapper;

    public List<Map<String, Object>> getRealtimePositions() {
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Vehicle v : vehicles) {
            HashMap<String, Object> pos = new HashMap<String, Object>();
            pos.put("vehicleId", v.getId());
            pos.put("name", v.getName());
            pos.put("status", v.getStatus());
            pos.put("battery", v.getBattery());
            pos.put("latitude", v.getLatitude() != null ? v.getLatitude() : new BigDecimal("31.2304"));
            pos.put("longitude", v.getLongitude() != null ? v.getLongitude() : new BigDecimal("121.4737"));
            pos.put("heading", 0);
            pos.put("speed", 0);
            pos.put("lastUpdate", v.getLastUpdate());
            result.add(pos);
        }
        return result;
    }

    public List<VehicleTrack> getVehicleTrack(String vehicleId, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return this.trackMapper.selectList(new LambdaQueryWrapper<VehicleTrack>().eq(VehicleTrack::getVehicleId, vehicleId).ge(VehicleTrack::getRecordedAt, since).orderByAsc(VehicleTrack::getRecordedAt));
    }

    public void recordTelemetry(String vehicleId, BigDecimal lat, BigDecimal lng, BigDecimal battery, BigDecimal speed) {
        VehicleTrack track = new VehicleTrack();
        track.setVehicleId(vehicleId);
        track.setLatitude(lat);
        track.setLongitude(lng);
        track.setBattery(battery);
        track.setSpeed(speed);
        track.setRecordedAt(LocalDateTime.now());
        this.trackMapper.insert(track);
        Vehicle vehicle = (Vehicle)this.vehicleMapper.selectById((Serializable)(vehicleId));
        if (vehicle != null) {
            vehicle.setLatitude(lat);
            vehicle.setLongitude(lng);
            vehicle.setBattery(battery);
            vehicle.setLastUpdate(LocalDateTime.now());
            this.vehicleMapper.updateById(vehicle);
        }
    }

    public DigitalTwinService(VehicleMapper vehicleMapper, VehicleTrackMapper trackMapper) {
        this.vehicleMapper = vehicleMapper;
        this.trackMapper = trackMapper;
    }
}
