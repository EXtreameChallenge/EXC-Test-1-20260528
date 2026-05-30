package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.MaintenancePrediction;
import com.claw.service.MaintenancePredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u9884\u6d4b\u6027\u7ef4\u62a4")
@RestController
@RequestMapping(value={"/api/v1/maintenance/prediction"})
public class MaintenancePredictionController {
    private final MaintenancePredictionService predictionService;

    @Operation(summary="\u7ef4\u62a4\u9884\u6d4b\u603b\u89c8")
    @GetMapping(value={"/overview"})
    public Result<Map<String, Object>> overview() {
        return Result.success(this.predictionService.getOverview());
    }

    @Operation(summary="\u9884\u6d4b\u5217\u8868")
    @GetMapping
    public Result<List<MaintenancePrediction>> list(@RequestParam(required=false) String status, @RequestParam(required=false) String vehicleId) {
        return Result.success(this.predictionService.listPredictions(status, vehicleId));
    }

    @Operation(summary="\u8f66\u8f86\u7ef4\u62a4\u9884\u6d4b")
    @GetMapping(value={"/vehicles/{vehicleId}"})
    public Result<List<MaintenancePrediction>> vehiclePredictions(@PathVariable String vehicleId) {
        return Result.success(this.predictionService.getVehiclePredictions(vehicleId));
    }

    @Operation(summary="\u5237\u65b0\u9884\u6d4b\u6570\u636e")
    @PostMapping(value={"/refresh"})
    @PreAuthorize(value="hasAuthority('PERM_fault:manage')")
    public Result<Void> refresh() {
        this.predictionService.refreshPredictions();
        return Result.success();
    }

    public MaintenancePredictionController(MaintenancePredictionService predictionService) {
        this.predictionService = predictionService;
    }
}
