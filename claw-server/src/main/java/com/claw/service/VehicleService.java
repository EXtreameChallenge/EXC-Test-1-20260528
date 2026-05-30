package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.PageResult;
import com.claw.entity.Vehicle;
import com.claw.mapper.VehicleMapper;
import com.claw.service.WebSocketService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class VehicleService {
    private final VehicleMapper vehicleMapper;
    private final WebSocketService webSocketService;

    public PageResult<Vehicle> listVehicles(int page, int size, String status, String type, String keyword) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText((String)status)) {
            wrapper.eq(Vehicle::getStatus, status);
        }
        if (StringUtils.hasText((String)type)) {
            wrapper.eq(Vehicle::getType, type);
        }
        if (StringUtils.hasText((String)keyword)) {
            wrapper.and(w -> w.like(Vehicle::getId, keyword).or().like(Vehicle::getName, keyword).or().like(Vehicle::getLocation, keyword));
        }
        wrapper.orderByDesc(Vehicle::getUpdatedAt);
        Page<Vehicle> pageResult = this.vehicleMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<Vehicle>(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    public Vehicle getVehicle(String id) {
        Vehicle vehicle = (Vehicle)this.vehicleMapper.selectById((Serializable)(id));
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "\u8f66\u8f86\u4e0d\u5b58\u5728: " + id);
        }
        return vehicle;
    }

    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setLastUpdate(LocalDateTime.now());
        this.vehicleMapper.insert(vehicle);
        return vehicle;
    }

    @Transactional
    public Vehicle updateVehicle(String id, Vehicle vehicle) {
        Vehicle existing = this.getVehicle(id);
        if (vehicle.getName() != null) {
            existing.setName(vehicle.getName());
        }
        if (vehicle.getModel() != null) {
            existing.setModel(vehicle.getModel());
        }
        if (vehicle.getType() != null) {
            existing.setType(vehicle.getType());
        }
        if (vehicle.getLocation() != null) {
            existing.setLocation(vehicle.getLocation());
        }
        if (vehicle.getLatitude() != null) {
            existing.setLatitude(vehicle.getLatitude());
        }
        if (vehicle.getLongitude() != null) {
            existing.setLongitude(vehicle.getLongitude());
        }
        existing.setLastUpdate(LocalDateTime.now());
        this.vehicleMapper.updateById(existing);
        return existing;
    }

    @Transactional
    public void updateVehicleStatus(String id, String status) {
        Vehicle vehicle = this.getVehicle(id);
        this.validateStatusTransition(vehicle.getStatus(), status);
        vehicle.setStatus(status);
        vehicle.setLastUpdate(LocalDateTime.now());
        this.vehicleMapper.updateById(vehicle);
        this.webSocketService.broadcastVehicleUpdate(id, status);
    }

    @Transactional
    public void deleteVehicle(String id) {
        Vehicle vehicle = this.getVehicle(id);
        if ("delivering".equals(vehicle.getStatus())) {
            throw new BusinessException(ErrorCode.VEHICLE_IN_DELIVERY);
        }
        this.vehicleMapper.deleteById((Serializable)(id));
    }

    public List<Vehicle> listAllVehicles() {
        return this.vehicleMapper.selectList(null);
    }

    public List<java.util.Map<String, Object>> getVehicleDispatchSummary() {
        return this.vehicleMapper.selectVehicleDispatchSummary();
    }

    private void validateStatusTransition(String current, String target) {
        if (current.equals(target)) return;
        java.util.Map<String, java.util.Set<String>> allowed = java.util.Map.of(
            "standby", java.util.Set.of("delivering", "charging", "fault"),
            "delivering", java.util.Set.of("standby", "fault"),
            "charging", java.util.Set.of("standby", "fault"),
            "fault", java.util.Set.of("standby")
        );
        java.util.Set<String> allowedTargets = allowed.get(current);
        if (allowedTargets == null || !allowedTargets.contains(target)) {
            throw new BusinessException(ErrorCode.VEHICLE_FAULT);
        }
    }

    public VehicleService(VehicleMapper vehicleMapper, WebSocketService webSocketService) {
        this.vehicleMapper = vehicleMapper;
        this.webSocketService = webSocketService;
    }
}
