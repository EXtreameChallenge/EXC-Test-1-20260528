package com.claw.controller;

import com.claw.common.PageResult;
import com.claw.common.Result;
import com.claw.entity.DispatchTask;
import com.claw.service.DispatchTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u4efb\u52a1\u8c03\u5ea6")
@RestController
@RequestMapping(value={"/api/v1/tasks"})
public class DispatchTaskController {
    private final DispatchTaskService taskService;

    @Operation(summary="\u4efb\u52a1\u5217\u8868")
    @GetMapping
    public Result<PageResult<DispatchTask>> list(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int size, @RequestParam(required=false) String status, @RequestParam(required=false) String priority, @RequestParam(required=false) String keyword) {
        return Result.success(this.taskService.listTasks(page, size, status, priority, keyword));
    }

    @Operation(summary="\u4efb\u52a1\u8be6\u60c5")
    @GetMapping(value={"/{id}"})
    public Result<DispatchTask> get(@PathVariable String id) {
        return Result.success(this.taskService.getTask(id));
    }

    @Operation(summary="\u521b\u5efa\u4efb\u52a1")
    @PostMapping
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<DispatchTask> create(@RequestBody DispatchTask task) {
        return Result.success(this.taskService.createTask(task));
    }

    @Operation(summary="\u66f4\u65b0\u4efb\u52a1\u72b6\u6001")
    @PutMapping(value={"/{id}/status"})
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<Void> updateStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        this.taskService.updateTaskStatus(id, body.get("status"));
        return Result.success();
    }

    @Operation(summary="\u5206\u914d\u8f66\u8f86")
    @PostMapping(value={"/{id}/assign"})
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<Void> assignVehicle(@PathVariable String id, @RequestBody Map<String, String> body) {
        this.taskService.assignVehicle(id, body.get("vehicleId"));
        return Result.success();
    }

    @Operation(summary="\u5220\u9664\u4efb\u52a1")
    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="hasAuthority('PERM_task:dispatch')")
    public Result<Void> delete(@PathVariable String id) {
        this.taskService.deleteTask(id);
        return Result.success();
    }

    public DispatchTaskController(DispatchTaskService taskService) {
        this.taskService = taskService;
    }
}
