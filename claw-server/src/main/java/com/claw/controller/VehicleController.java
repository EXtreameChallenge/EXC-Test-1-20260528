package com.claw.controller;

import com.claw.common.PageResult;
import com.claw.common.Result;
import com.claw.entity.Vehicle;
import com.claw.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u8f66\u8f86\u7ba1\u7406")
@RestController
@RequestMapping(value={"/api/v1/vehicles"})
public class VehicleController {
    private final VehicleService vehicleService;

    @Operation(summary="\u8f66\u8f86\u5217\u8868(\u5206\u9875)")
    @GetMapping
    public Result<PageResult<Vehicle>> list(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int size, @RequestParam(required=false) String status, @RequestParam(required=false) String type, @RequestParam(required=false) String keyword) {
        return Result.success(this.vehicleService.listVehicles(page, size, status, type, keyword));
    }

    @Operation(summary="\u6240\u6709\u8f66\u8f86(\u4e0d\u5206\u9875)")
    @GetMapping(value={"/all"})
    public Result<List<Vehicle>> listAll() {
        return Result.success(this.vehicleService.listAllVehicles());
    }

    @Operation(summary="\u8f66\u8f86\u8c03\u5ea6\u6c47\u603b(XML\u591a\u8868\u8054\u67e5)")
    @GetMapping(value={"/dispatch-summary"})
    public Result<List<Map<String, Object>>> dispatchSummary() {
        return Result.success(this.vehicleService.getVehicleDispatchSummary());
    }

    @Operation(summary="\u8f66\u8f86\u8be6\u60c5")
    @GetMapping(value={"/{id}"})
    public Result<Vehicle> get(@PathVariable String id) {
        return Result.success(this.vehicleService.getVehicle(id));
    }

    @Operation(summary="\u65b0\u589e\u8f66\u8f86")
    @PostMapping
    public Result<Vehicle> create(@RequestBody Vehicle vehicle) {
        return Result.success(this.vehicleService.createVehicle(vehicle));
    }

    @Operation(summary="\u66f4\u65b0\u8f66\u8f86")
    @PutMapping(value={"/{id}"})
    public Result<Vehicle> update(@PathVariable String id, @RequestBody Vehicle vehicle) {
        return Result.success(this.vehicleService.updateVehicle(id, vehicle));
    }

    @Operation(summary="\u66f4\u65b0\u8f66\u8f86\u72b6\u6001")
    @PutMapping(value={"/{id}/status"})
    public Result<Void> updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        this.vehicleService.updateVehicleStatus(id, body.get("status"));
        return Result.success();
    }

    @Operation(summary="\u5220\u9664\u8f66\u8f86")
    @DeleteMapping(value={"/{id}"})
    public Result<Void> delete(@PathVariable String id) {
        this.vehicleService.deleteVehicle(id);
        return Result.success();
    }

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
}
