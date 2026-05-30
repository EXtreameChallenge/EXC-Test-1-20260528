package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.VehicleTrack;
import com.claw.service.DigitalTwinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u6570\u5b57\u5b6a\u751f")
@RestController
@RequestMapping(value={"/api/v1/twin"})
public class DigitalTwinController {
    private final DigitalTwinService twinService;

    @Operation(summary="\u6240\u6709\u8f66\u8f86\u5b9e\u65f6\u4f4d\u7f6e")
    @GetMapping(value={"/vehicles/realtime"})
    public Result<List<Map<String, Object>>> realtime() {
        return Result.success(this.twinService.getRealtimePositions());
    }

    @Operation(summary="\u5355\u8f66\u8f68\u8ff9\u56de\u653e")
    @GetMapping(value={"/vehicles/{id}/track"})
    public Result<List<VehicleTrack>> track(@PathVariable String id, @RequestParam(defaultValue="24") int hours) {
        return Result.success(this.twinService.getVehicleTrack(id, hours));
    }

    @Operation(summary="\u4e0a\u62a5\u9065\u6d4b\u6570\u636e")
    @PostMapping(value={"/vehicles/{id}/telemetry"})
    @PreAuthorize(value="hasAuthority('PERM_vehicle:manage')")
    public Result<Void> telemetry(@PathVariable String id, @RequestBody Map<String, Object> data) {
        Object latObj = data.get("latitude");
        Object lngObj = data.get("longitude");
        Object batObj = data.get("battery");
        if (latObj == null || lngObj == null || batObj == null) {
            return Result.error(400, "latitude, longitude, battery are required");
        }
        this.twinService.recordTelemetry(id, new BigDecimal(latObj.toString()), new BigDecimal(lngObj.toString()), new BigDecimal(batObj.toString()), new BigDecimal(data.getOrDefault("speed", "0").toString()));
        return Result.success();
    }

    public DigitalTwinController(DigitalTwinService twinService) {
        this.twinService = twinService;
    }
}
