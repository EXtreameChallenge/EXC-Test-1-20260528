package com.claw.controller;

import com.claw.common.PageResult;
import com.claw.common.Result;
import com.claw.entity.SysRole;
import com.claw.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="角色管理")
@RestController
@RequestMapping(value={"/api/v1/roles"})
public class RoleController {
    private final SysRoleService roleService;

    public RoleController(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary="角色列表(分页)")
    @GetMapping
    public Result<PageResult<SysRole>> list(@RequestParam(defaultValue="1") int page,
                                             @RequestParam(defaultValue="20") int size,
                                             @RequestParam(required=false) String keyword) {
        return Result.success(this.roleService.listRoles(page, size, keyword));
    }

    @Operation(summary="角色详情")
    @GetMapping(value={"/{id}"})
    public Result<SysRole> get(@PathVariable Long id) {
        return Result.success(this.roleService.getRoleById(id));
    }

    @Operation(summary="新增角色")
    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        return Result.success(this.roleService.createRole(role));
    }

    @Operation(summary="更新角色")
    @PutMapping(value={"/{id}"})
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        return Result.success(this.roleService.updateRole(id, role));
    }

    @Operation(summary="删除角色")
    @DeleteMapping(value={"/{id}"})
    public Result<Void> delete(@PathVariable Long id) {
        this.roleService.deleteRole(id);
        return Result.success();
    }
}
