package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.ChargingStation;
import com.claw.service.EnergyService;
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

@Tag(name="\u80fd\u6e90\u4f18\u5316")
@RestController
@RequestMapping(value={"/api/v1/energy"})
public class EnergyController {
    private final EnergyService energyService;

    @Operation(summary="\u5145\u7535\u7ad9\u5217\u8868")
    @GetMapping(value={"/charging-stations"})
    public Result<List<ChargingStation>> listStations() {
        return Result.success(this.energyService.listStations());
    }

    @Operation(summary="\u5145\u7535\u7ad9\u8be6\u60c5")
    @GetMapping(value={"/charging-stations/{id}"})
    public Result<ChargingStation> getStation(@PathVariable Long id) {
        return Result.success(this.energyService.getStation(id));
    }

    @Operation(summary="\u521b\u5efa\u5145\u7535\u7ad9")
    @PostMapping(value={"/charging-stations"})
    @PreAuthorize(value="hasAuthority('PERM_energy:manage')")
    public Result<ChargingStation> createStation(@RequestBody ChargingStation station) {
        return Result.success(this.energyService.createStation(station));
    }

    @Operation(summary="\u66f4\u65b0\u5145\u7535\u7ad9")
    @PutMapping(value={"/charging-stations/{id}"})
    @PreAuthorize(value="hasAuthority('PERM_energy:manage')")
    public Result<ChargingStation> updateStation(@PathVariable Long id, @RequestBody ChargingStation station) {
        return Result.success(this.energyService.updateStation(id, station));
    }

    @Operation(summary="\u5220\u9664\u5145\u7535\u7ad9")
    @DeleteMapping(value={"/charging-stations/{id}"})
    @PreAuthorize(value="hasAuthority('PERM_energy:manage')")
    public Result<Void> deleteStation(@PathVariable Long id) {
        this.energyService.deleteStation(id);
        return Result.success();
    }

    @Operation(summary="\u80fd\u6e90\u4f18\u5316\u5efa\u8bae")
    @GetMapping(value={"/optimize"})
    public Result<Map<String, Object>> optimize() {
        return Result.success(this.energyService.getOptimization());
    }

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }
}
