package com.claw.controller;

import com.claw.common.PageResult;
import com.claw.common.Result;
import com.claw.entity.FaultAlert;
import com.claw.service.FaultAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u544a\u8b66\u7ba1\u7406")
@RestController
@RequestMapping(value={"/api/v1/alerts"})
public class FaultAlertController {
    private final FaultAlertService alertService;

    @Operation(summary="\u544a\u8b66\u5217\u8868")
    @GetMapping
    public Result<PageResult<FaultAlert>> list(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int size, @RequestParam(required=false) String level, @RequestParam(required=false) String status) {
        return Result.success(this.alertService.listAlerts(page, size, level, status));
    }

    @Operation(summary="\u544a\u8b66\u8be6\u60c5")
    @GetMapping(value={"/{id}"})
    public Result<FaultAlert> get(@PathVariable String id) {
        return Result.success(this.alertService.getAlert(id));
    }

    @Operation(summary="\u786e\u8ba4\u544a\u8b66")
    @PutMapping(value={"/{id}/confirm"})
    public Result<Void> confirm(@PathVariable String id, Authentication auth) {
        this.alertService.confirmAlert(id, (Long)auth.getPrincipal());
        return Result.success();
    }

    @Operation(summary="\u89e3\u51b3\u544a\u8b66")
    @PutMapping(value={"/{id}/resolve"})
    public Result<Void> resolve(@PathVariable String id) {
        this.alertService.resolveAlert(id);
        return Result.success();
    }

    @Operation(summary="\u5168\u90e8\u6807\u8bb0\u5df2\u8bfb")
    @PutMapping(value={"/read-all"})
    public Result<Void> readAll() {
        this.alertService.readAll();
        return Result.success();
    }

    public FaultAlertController(FaultAlertService alertService) {
        this.alertService = alertService;
    }
}
