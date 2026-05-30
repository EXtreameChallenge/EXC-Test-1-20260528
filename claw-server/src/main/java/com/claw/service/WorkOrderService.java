package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.PageResult;
import com.claw.entity.WorkOrder;
import com.claw.mapper.WorkOrderMapper;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class WorkOrderService {
    private final WorkOrderMapper orderMapper;

    public PageResult<WorkOrder> listOrders(int page, int size, String status, String priority) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText((String)status)) {
            wrapper.eq(WorkOrder::getStatus, status);
        }
        if (StringUtils.hasText((String)priority)) {
            wrapper.eq(WorkOrder::getPriority, priority);
        }
        wrapper.orderByDesc(WorkOrder::getCreatedAt);
        Page<WorkOrder> result = this.orderMapper.selectPage(new Page<>((long)page, (long)size), wrapper);
        return new PageResult<WorkOrder>(result.getRecords(), result.getTotal(), page, size);
    }

    public WorkOrder getOrder(String id) {
        WorkOrder order = (WorkOrder)this.orderMapper.selectById((Serializable)(id));
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "\u5de5\u5355\u4e0d\u5b58\u5728: " + id);
        }
        return order;
    }

    @Transactional
    public WorkOrder createOrder(WorkOrder order) {
        order.setStatus("pending");
        this.orderMapper.insert(order);
        return order;
    }

    @Transactional
    public void startProcessing(String id, Long assigneeId) {
        WorkOrder order = this.getOrder(id);
        order.setStatus("processing");
        order.setAssigneeId(assigneeId);
        this.orderMapper.updateById(order);
    }

    @Transactional
    public void completeOrder(String id) {
        WorkOrder order = this.getOrder(id);
        order.setStatus("completed");
        order.setCompletedAt(LocalDateTime.now());
        this.orderMapper.updateById(order);
    }

    public WorkOrderService(WorkOrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }
}
