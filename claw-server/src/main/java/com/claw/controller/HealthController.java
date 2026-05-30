package com.claw.controller;

import com.claw.common.Result;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Value(value="${glm.api-key:}")
    private String glmApiKey;

    @GetMapping(value={"/api/health"})
    public Map<String, Object> health() {
        boolean aiReady = glmApiKey != null && !glmApiKey.trim().isEmpty();
        return Map.of(
            "status", aiReady ? "ok" : "partial",
            "service", "claw-server",
            "ai", aiReady ? "available" : "unavailable",
            "message", aiReady ? "GLM-4-Flash 服务可用" : "GLM API Key 未配置，AI功能不可用"
        );
    }
}
