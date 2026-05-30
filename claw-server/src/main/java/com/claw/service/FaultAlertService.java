package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.PageResult;
import com.claw.entity.FaultAlert;
import com.claw.mapper.FaultAlertMapper;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class FaultAlertService {
    private final FaultAlertMapper alertMapper;

    public PageResult<FaultAlert> listAlerts(int page, int size, String level, String status) {
        LambdaQueryWrapper<FaultAlert> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText((String)level)) {
            wrapper.eq(FaultAlert::getLevel, (Object)level);
        }
        if (StringUtils.hasText((String)status)) {
            wrapper.eq(FaultAlert::getStatus, status);
        }
        wrapper.orderByDesc(FaultAlert::getCreatedAt);
        Page<FaultAlert> result = this.alertMapper.selectPage(new Page<>((long)page, (long)size), wrapper);
        return new PageResult<FaultAlert>(result.getRecords(), result.getTotal(), page, size);
    }

    public FaultAlert getAlert(String id) {
        FaultAlert alert = (FaultAlert)this.alertMapper.selectById((Serializable)(id));
        if (alert == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "\u544a\u8b66\u4e0d\u5b58\u5728: " + id);
        }
        return alert;
    }

    @Transactional
    public void confirmAlert(String id, Long confirmedBy) {
        FaultAlert alert = this.getAlert(id);
        if (!"unconfirmed".equals(alert.getStatus())) {
            throw new BusinessException(ErrorCode.ALERT_ALREADY_CONFIRMED);
        }
        alert.setStatus("confirmed");
        alert.setConfirmedBy(confirmedBy);
        alert.setConfirmedAt(LocalDateTime.now());
        this.alertMapper.updateById(alert);
    }

    @Transactional
    public void resolveAlert(String id) {
        FaultAlert alert = this.getAlert(id);
        alert.setStatus("resolved");
        this.alertMapper.updateById(alert);
    }

    @Transactional
    public void readAll() {
        this.alertMapper.update(null, new LambdaUpdateWrapper<FaultAlert>().eq(FaultAlert::getStatus, "unconfirmed").set(FaultAlert::getStatus, "confirmed"));
    }

    @Transactional
    public FaultAlert createAlert(FaultAlert alert) {
        alert.setStatus("unconfirmed");
        this.alertMapper.insert(alert);
        return alert;
    }

    public FaultAlertService(FaultAlertMapper alertMapper) {
        this.alertMapper = alertMapper;
    }
}
