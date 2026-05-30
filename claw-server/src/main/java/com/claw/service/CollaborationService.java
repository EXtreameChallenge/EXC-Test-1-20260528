package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.claw.entity.CollaborationEvent;
import com.claw.mapper.CollaborationEventMapper;
import com.claw.service.WebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CollaborationService {
    private final CollaborationEventMapper eventMapper;
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper;

    public CollaborationEvent sendEvent(Long sourceUserId, String eventType, Long targetUserId, String sourceDevice, Map<String, Object> payload) {
        CollaborationEvent event = new CollaborationEvent();
        event.setEventType(eventType);
        event.setSourceUserId(sourceUserId);
        event.setTargetUserId(targetUserId);
        event.setSourceDevice(sourceDevice);
        try {
            event.setPayload(this.objectMapper.writeValueAsString(payload));
        }
        catch (Exception e) {
            event.setPayload("{}");
        }
        this.eventMapper.insert(event);
        if (targetUserId != null) {
            this.webSocketService.pushCollabEvent(targetUserId, Map.of("eventId", event.getId(), "eventType", eventType, "sourceUserId", sourceUserId, "sourceDevice", sourceDevice, "payload", payload, "timestamp", System.currentTimeMillis()));
        }
        return event;
    }

    public List<CollaborationEvent> getPendingEvents(Long userId) {
        return this.eventMapper.selectList(new LambdaQueryWrapper<CollaborationEvent>().eq(CollaborationEvent::getTargetUserId, userId).isNull(CollaborationEvent::getReadAt).orderByDesc(CollaborationEvent::getCreatedAt));
    }

    public List<CollaborationEvent> getAllEvents(Long userId, int limit) {
        return this.eventMapper.selectList(new LambdaQueryWrapper<CollaborationEvent>().eq(CollaborationEvent::getTargetUserId, userId).orderByDesc(CollaborationEvent::getCreatedAt).last("LIMIT " + Math.min(Math.max(limit, 1), 100)));
    }

    public void markAsRead(Long eventId, Long userId) {
        this.eventMapper.update(null, new LambdaUpdateWrapper<CollaborationEvent>().eq(CollaborationEvent::getId, eventId).eq(CollaborationEvent::getTargetUserId, userId).set(CollaborationEvent::getReadAt, LocalDateTime.now()));
    }

    public void markAllAsRead(Long userId) {
        this.eventMapper.update(null, new LambdaUpdateWrapper<CollaborationEvent>().eq(CollaborationEvent::getTargetUserId, userId).isNull(CollaborationEvent::getReadAt).set(CollaborationEvent::getReadAt, LocalDateTime.now()));
    }

    public long getUnreadCount(Long userId) {
        return this.eventMapper.selectCount(new LambdaQueryWrapper<CollaborationEvent>().eq(CollaborationEvent::getTargetUserId, userId).isNull(CollaborationEvent::getReadAt));
    }

    public CollaborationService(CollaborationEventMapper eventMapper, WebSocketService webSocketService, ObjectMapper objectMapper) {
        this.eventMapper = eventMapper;
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }
}
