package com.claw.controller;

import com.claw.common.Result;
import com.claw.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u8fd0\u8425\u5206\u6790")
@RestController
@RequestMapping(value={"/api/v1/analytics"})
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(summary="\u4eea\u8868\u76d8\u6570\u636e")
    @GetMapping(value={"/dashboard"})
    public Result<Map<String, Object>> dashboard() {
        return Result.success(this.analyticsService.getDashboardData());
    }

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
}
