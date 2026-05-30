package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.DispatchTask;
import com.claw.service.DispatchPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u534f\u540c\u8c03\u5ea6")
@RestController
@RequestMapping(value={"/api/v1/dispatch/plan"})
public class DispatchPlanController {
    private final DispatchPlanService dispatchPlanService;

    @Operation(summary="AI\u4f18\u5316\u8c03\u5ea6")
    @PostMapping(value={"/optimize"})
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<Map<String, Object>> optimize(@RequestBody Map<String, Object> params, Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        String targetArea = (String)params.getOrDefault("targetArea", "");
        int vehicleCount = params.containsKey("vehicleCount") ? ((Number)params.get("vehicleCount")).intValue() : 3;
        String priority = (String)params.getOrDefault("priority", "medium");
        return Result.success(this.dispatchPlanService.optimize(targetArea, vehicleCount, priority, userId));
    }

    @Operation(summary="\u6267\u884c\u8c03\u5ea6\u8ba1\u5212")
    @PostMapping(value={"/execute"})
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<List<DispatchTask>> execute(@RequestBody Map<String, Object> plan, Authentication auth) {
        Long userId = (Long)auth.getPrincipal();
        return Result.success(this.dispatchPlanService.executePlan(plan, userId));
    }

    public DispatchPlanController(DispatchPlanService dispatchPlanService) {
        this.dispatchPlanService = dispatchPlanService;
    }
}
