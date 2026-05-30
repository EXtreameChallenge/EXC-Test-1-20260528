package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.ChargingStation;
import com.claw.entity.Vehicle;
import com.claw.mapper.ChargingStationMapper;
import com.claw.mapper.VehicleMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EnergyService {
    private final ChargingStationMapper stationMapper;
    private final VehicleMapper vehicleMapper;

    public List<ChargingStation> listStations() {
        return this.stationMapper.selectList(null);
    }

    public ChargingStation getStation(Long id) {
        return (ChargingStation)this.stationMapper.selectById(id);
    }

    public ChargingStation createStation(ChargingStation station) {
        this.stationMapper.insert(station);
        return station;
    }

    public ChargingStation updateStation(Long id, ChargingStation station) {
        station.setId(id);
        this.stationMapper.updateById(station);
        return station;
    }

    public void deleteStation(Long id) {
        this.stationMapper.deleteById(id);
    }

    public Map<String, Object> getOptimization() {
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        List<ChargingStation> stations = this.stationMapper.selectList(new LambdaQueryWrapper<ChargingStation>().eq(ChargingStation::getStatus, (Object)1));
        long lowBatteryCount = vehicles.stream().filter(v -> v.getBattery() != null && v.getBattery().compareTo(new BigDecimal("20")) < 0).count();
        long chargingCount = vehicles.stream().filter(v -> "charging".equals(v.getStatus())).count();
        long needChargingCount = vehicles.stream().filter(v -> v.getBattery() != null && v.getBattery().compareTo(new BigDecimal("30")) < 0 && !"charging".equals(v.getStatus())).count();
        int totalSlots = stations.stream().mapToInt(ChargingStation::getTotalSlots).sum();
        int availableSlots = stations.stream().mapToInt(ChargingStation::getAvailableSlots).sum();
        int queueCount = stations.stream().mapToInt(ChargingStation::getQueueCount).sum();
        double avgBattery = vehicles.stream().mapToDouble(v -> v.getBattery() != null ? v.getBattery().doubleValue() : 0.0).average().orElse(0.0);
        List<Map<String, Object>> recommendations = this.generateRecommendations(vehicles, stations);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("totalVehicles", vehicles.size());
        result.put("lowBatteryCount", lowBatteryCount);
        result.put("chargingCount", chargingCount);
        result.put("needChargingCount", needChargingCount);
        result.put("avgBattery", (double)Math.round(avgBattery * 10.0) / 10.0);
        result.put("totalChargingSlots", totalSlots);
        result.put("availableSlots", availableSlots);
        result.put("queueCount", queueCount);
        result.put("utilizationRate", totalSlots > 0 ? (double)Math.round((1.0 - (double)availableSlots / (double)totalSlots) * 1000.0) / 10.0 : 0.0);
        result.put("recommendations", recommendations);
        return result;
    }

    private List<Map<String, Object>> generateRecommendations(List<Vehicle> vehicles, List<ChargingStation> stations) {
        ArrayList<Map<String, Object>> recs = new ArrayList<Map<String, Object>>();
        for (Vehicle v : vehicles) {
            ChargingStation bestStation;
            if (v.getBattery() == null || v.getBattery().compareTo(new BigDecimal("30")) >= 0 || "charging".equals(v.getStatus()) || (bestStation = this.findNearestStation(v, stations)) == null) continue;
            LinkedHashMap<String, Object> rec = new LinkedHashMap<String, Object>();
            rec.put("vehicleId", v.getId());
            rec.put("vehicleName", v.getName());
            rec.put("battery", v.getBattery());
            rec.put("stationId", bestStation.getId());
            rec.put("stationName", bestStation.getName());
            rec.put("availableSlots", bestStation.getAvailableSlots());
            rec.put("urgency", v.getBattery().compareTo(new BigDecimal("15")) < 0 ? "urgent" : "normal");
            recs.add(rec);
        }
        recs.sort((a, b) -> {
            String ua = (String)a.get("urgency");
            String ub = (String)b.get("urgency");
            if ("urgent".equals(ua) && !"urgent".equals(ub)) {
                return -1;
            }
            if (!"urgent".equals(ua) && "urgent".equals(ub)) {
                return 1;
            }
            return ((BigDecimal)a.get("battery")).compareTo((BigDecimal)b.get("battery"));
        });
        return recs;
    }

    private ChargingStation findNearestStation(Vehicle vehicle, List<ChargingStation> stations) {
        if (vehicle.getLatitude() == null || vehicle.getLongitude() == null || stations.isEmpty()) {
            return stations.isEmpty() ? null : stations.get(0);
        }
        ChargingStation nearest = null;
        double minDist = Double.MAX_VALUE;
        for (ChargingStation s : stations) {
            double dist;
            if (s.getAvailableSlots() <= 0 || !((dist = this.haversine(vehicle.getLatitude().doubleValue(), vehicle.getLongitude().doubleValue(), s.getLatitude().doubleValue(), s.getLongitude().doubleValue())) < minDist)) continue;
            minDist = dist;
            nearest = s;
        }
        return nearest != null ? nearest : stations.get(0);
    }

    private double haversine(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return R * c;
    }

    public EnergyService(ChargingStationMapper stationMapper, VehicleMapper vehicleMapper) {
        this.stationMapper = stationMapper;
        this.vehicleMapper = vehicleMapper;
    }
}
