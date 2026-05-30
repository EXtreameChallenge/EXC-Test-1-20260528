package com.claw.controller;

import com.claw.common.PageResult;
import com.claw.common.Result;
import com.claw.entity.WorkOrder;
import com.claw.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u7ef4\u4fee\u5de5\u5355")
@RestController
@RequestMapping(value={"/api/v1/work-orders"})
public class WorkOrderController {
    private final WorkOrderService orderService;

    @Operation(summary="\u5de5\u5355\u5217\u8868")
    @GetMapping
    public Result<PageResult<WorkOrder>> list(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int size, @RequestParam(required=false) String status, @RequestParam(required=false) String priority) {
        return Result.success(this.orderService.listOrders(page, size, status, priority));
    }

    @Operation(summary="\u521b\u5efa\u5de5\u5355")
    @PostMapping
    @PreAuthorize(value="hasAuthority('PERM_fault:manage')")
    public Result<WorkOrder> create(@RequestBody WorkOrder order) {
        return Result.success(this.orderService.createOrder(order));
    }

    @Operation(summary="\u5f00\u59cb\u5904\u7406")
    @PutMapping(value={"/{id}/start"})
    @PreAuthorize(value="hasAuthority('PERM_fault:manage')")
    public Result<Void> startProcessing(@PathVariable String id, Authentication auth) {
        this.orderService.startProcessing(id, (Long)auth.getPrincipal());
        return Result.success();
    }

    @Operation(summary="\u5b8c\u6210\u5de5\u5355")
    @PutMapping(value={"/{id}/complete"})
    @PreAuthorize(value="hasAuthority('PERM_fault:manage')")
    public Result<Void> complete(@PathVariable String id) {
        this.orderService.completeOrder(id);
        return Result.success();
    }

    public WorkOrderController(WorkOrderService orderService) {
        this.orderService = orderService;
    }
}
