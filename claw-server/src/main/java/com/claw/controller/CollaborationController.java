package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.CollaborationEvent;
import com.claw.service.CollaborationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u8de8\u7aef\u534f\u4f5c")
@RestController
@RequestMapping(value={"/api/v1/collab"})
public class CollaborationController {
    private final CollaborationService collaborationService;

    @Operation(summary="\u53d1\u9001\u534f\u4f5c\u4e8b\u4ef6")
    @PostMapping(value={"/events"})
    public Result<CollaborationEvent> sendEvent(@RequestBody Map<String, Object> body, Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        String eventType = (String)body.get("eventType");
        Long targetUserId = body.get("targetUserId") != null ? Long.valueOf(((Number)body.get("targetUserId")).longValue()) : null;
        String sourceDevice = (String)body.getOrDefault("sourceDevice", "pc");
        @SuppressWarnings("unchecked") Map<String, Object> payload = (Map<String, Object>)body.getOrDefault("payload", Map.of());
        return Result.success(this.collaborationService.sendEvent(userId, eventType, targetUserId, sourceDevice, payload));
    }

    @Operation(summary="\u5f85\u5904\u7406\u534f\u4f5c\u4e8b\u4ef6")
    @GetMapping(value={"/events/pending"})
    public Result<List<CollaborationEvent>> pendingEvents(Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        return Result.success(this.collaborationService.getPendingEvents(userId));
    }

    @Operation(summary="\u6240\u6709\u534f\u4f5c\u4e8b\u4ef6")
    @GetMapping(value={"/events"})
    public Result<List<CollaborationEvent>> allEvents(Authentication auth, @RequestParam(defaultValue="50") int limit) {
        Long userId = (Long)auth.getPrincipal();
        return Result.success(this.collaborationService.getAllEvents(userId, limit));
    }

    @Operation(summary="\u6807\u8bb0\u5df2\u8bfb")
    @PutMapping(value={"/events/{id}/read"})
    public Result<Void> markRead(@PathVariable Long id, Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        this.collaborationService.markAsRead(id, userId);
        return Result.success();
    }

    @Operation(summary="\u5168\u90e8\u6807\u8bb0\u5df2\u8bfb")
    @PutMapping(value={"/events/read-all"})
    public Result<Void> markAllRead(Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        this.collaborationService.markAllAsRead(userId);
        return Result.success();
    }

    @Operation(summary="\u672a\u8bfb\u6570\u91cf")
    @GetMapping(value={"/events/unread-count"})
    public Result<Map<String, Object>> unreadCount(Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        return Result.success(Map.of("count", this.collaborationService.getUnreadCount(userId)));
    }

    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }
}
