package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.PageResult;
import com.claw.entity.DispatchTask;
import com.claw.entity.Vehicle;
import com.claw.mapper.DispatchTaskMapper;
import com.claw.mapper.VehicleMapper;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DispatchTaskService {
    private final DispatchTaskMapper taskMapper;
    private final VehicleMapper vehicleMapper;
    private static final Set<String> VALID_TRANSITIONS = Set.of("pending->confirmed", "pending->cancelled", "confirmed->executing", "confirmed->cancelled", "executing->completed", "executing->cancelled");

    public PageResult<DispatchTask> listTasks(int page, int size, String status, String priority, String keyword) {
        LambdaQueryWrapper<DispatchTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText((String)status)) {
            wrapper.eq(DispatchTask::getStatus, status);
        }
        if (StringUtils.hasText((String)priority)) {
            wrapper.eq(DispatchTask::getPriority, priority);
        }
        if (StringUtils.hasText((String)keyword)) {
            wrapper.and(w -> w.like(DispatchTask::getId, keyword).or().like(DispatchTask::getName, keyword));
        }
        wrapper.orderByDesc(DispatchTask::getCreatedAt);
        Page<DispatchTask> result = this.taskMapper.selectPage(new Page<>((long)page, (long)size), wrapper);
        return new PageResult<DispatchTask>(result.getRecords(), result.getTotal(), page, size);
    }

    public DispatchTask getTask(String id) {
        DispatchTask task = (DispatchTask)this.taskMapper.selectById((Serializable)(id));
        if (task == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "\u4efb\u52a1\u4e0d\u5b58\u5728: " + id);
        }
        return task;
    }

    @Transactional
    public DispatchTask createTask(DispatchTask task) {
        task.setStatus("pending");
        this.taskMapper.insert(task);
        return task;
    }

    @Transactional
    public void updateTaskStatus(String id, String newStatus) {
        DispatchTask task = this.getTask(id);
        String transition = task.getStatus() + "->" + newStatus;
        if (!VALID_TRANSITIONS.contains(transition)) {
            throw new BusinessException(ErrorCode.TASK_STATUS_INVALID, "\u4e0d\u5141\u8bb8\u4ece" + task.getStatus() + "\u53d8\u66f4\u4e3a" + newStatus);
        }
        task.setStatus(newStatus);
        if ("completed".equals(newStatus)) {
            Vehicle v;
            task.setCompletedTime(LocalDateTime.now());
            if (task.getVehicleId() != null && (v = (Vehicle)this.vehicleMapper.selectById((Serializable)((Object)task.getVehicleId()))) != null) {
                v.setStatus("standby");
                v.setLastUpdate(LocalDateTime.now());
                this.vehicleMapper.updateById(v);
            }
        }
        this.taskMapper.updateById(task);
    }

    @Transactional
    public void assignVehicle(String id, String vehicleId) {
        DispatchTask task = this.getTask(id);
        Vehicle vehicle = (Vehicle)this.vehicleMapper.selectById((Serializable)(vehicleId));
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "\u8f66\u8f86\u4e0d\u5b58\u5728");
        }
        if (!"standby".equals(vehicle.getStatus())) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_AVAILABLE, "\u8f66\u8f86\u5f53\u524d\u72b6\u6001\u4e0d\u53ef\u5206\u914d");
        }
        task.setVehicleId(vehicleId);
        this.taskMapper.updateById(task);
    }

    @Transactional
    public void deleteTask(String id) {
        DispatchTask task = this.getTask(id);
        if (!"pending".equals(task.getStatus()) && !"cancelled".equals(task.getStatus())) {
            throw new BusinessException(ErrorCode.TASK_CANNOT_DELETE);
        }
        this.taskMapper.deleteById((Serializable)(id));
    }

    public DispatchTaskService(DispatchTaskMapper taskMapper, VehicleMapper vehicleMapper) {
        this.taskMapper = taskMapper;
        this.vehicleMapper = vehicleMapper;
    }
}
