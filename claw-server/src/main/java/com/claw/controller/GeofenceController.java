package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.Geofence;
import com.claw.service.GeofenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u7535\u5b50\u56f4\u680f")
@RestController
@RequestMapping(value={"/api/v1/geofences"})
public class GeofenceController {
    private final GeofenceService geofenceService;

    @Operation(summary="\u56f4\u680f\u5217\u8868")
    @GetMapping
    public Result<List<Geofence>> list() {
        return Result.success(this.geofenceService.listGeofences());
    }

    @Operation(summary="\u56f4\u680f\u8be6\u60c5")
    @GetMapping(value={"/{id}"})
    public Result<Geofence> get(@PathVariable Long id) {
        return Result.success(this.geofenceService.getGeofence(id));
    }

    @Operation(summary="\u521b\u5efa\u56f4\u680f")
    @PostMapping
    @PreAuthorize(value="hasAuthority('PERM_fleet:manage')")
    public Result<Geofence> create(@RequestBody Geofence geofence) {
        return Result.success(this.geofenceService.createGeofence(geofence));
    }

    @Operation(summary="\u66f4\u65b0\u56f4\u680f")
    @PutMapping(value={"/{id}"})
    @PreAuthorize(value="hasAuthority('PERM_fleet:manage')")
    public Result<Geofence> update(@PathVariable Long id, @RequestBody Geofence geofence) {
        return Result.success(this.geofenceService.updateGeofence(id, geofence));
    }

    @Operation(summary="\u5220\u9664\u56f4\u680f")
    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="hasAuthority('PERM_fleet:manage')")
    public Result<Void> delete(@PathVariable Long id) {
        this.geofenceService.deleteGeofence(id);
        return Result.success();
    }

    @Operation(summary="\u68c0\u6d4b\u56f4\u680f\u8fdd\u89c4")
    @GetMapping(value={"/violations"})
    public Result<List<Map<String, Object>>> checkViolations() {
        return Result.success(this.geofenceService.checkViolations());
    }

    public GeofenceController(GeofenceService geofenceService) {
        this.geofenceService = geofenceService;
    }
}
