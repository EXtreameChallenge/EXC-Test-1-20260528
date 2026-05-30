package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.SysUser;
import com.claw.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户管理")
@RestController
@RequestMapping(value = {"/api/v1/users"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "根据ID查询用户信息")
    @GetMapping(value = {"/{id}"})
    public Result<SysUser> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @Operation(summary = "查询所有用户")
    @GetMapping
    public Result<List<SysUser>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<SysUser> createUser(@RequestBody Map<String, Object> body) {
        SysUser user = new SysUser();
        user.setUsername((String) body.get("username"));
        user.setPassword((String) body.get("password"));
        user.setName((String) body.get("name"));
        user.setEmail((String) body.get("email"));
        user.setPhone((String) body.get("phone"));
        user.setStatus(body.get("status") != null ? ((Number) body.get("status")).intValue() : 1);
        Long roleId = body.get("roleId") != null ? ((Number) body.get("roleId")).longValue() : null;
        return Result.success(userService.createUser(user, roleId));
    }

    @Operation(summary = "更新用户")
    @PutMapping(value = {"/{id}"})
    public Result<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        return Result.success(userService.updateUser(id, user));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping(value = {"/{id}"})
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
