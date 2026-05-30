package com.claw.service;

import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void pushVehicleUpdate(Long userId, String vehicleId, String newStatus) {
        this.messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/topic/vehicle", Map.of("vehicleId", vehicleId, "status", newStatus, "timestamp", System.currentTimeMillis()));
        this.messagingTemplate.convertAndSend("/topic/vehicle", Map.of("vehicleId", vehicleId, "status", newStatus, "timestamp", System.currentTimeMillis()));
    }

    public void pushNewAlert(Long userId, Map<String, Object> alert) {
        this.messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/topic/alert", alert);
        this.messagingTemplate.convertAndSend("/topic/alert", alert);
    }

    public void pushCollabEvent(Long userId, Map<String, Object> event) {
        this.messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/topic/collab", event);
    }

    public void broadcastVehicleUpdate(String vehicleId, String newStatus) {
        this.messagingTemplate.convertAndSend("/topic/vehicle", Map.of("vehicleId", vehicleId, "status", newStatus, "timestamp", System.currentTimeMillis()));
    }

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}
