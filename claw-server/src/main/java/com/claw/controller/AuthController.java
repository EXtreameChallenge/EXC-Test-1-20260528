package com.claw.controller;

import com.claw.aspect.OperationLog;
import com.claw.common.Result;
import com.claw.dto.request.LoginRequest;
import com.claw.dto.request.RefreshTokenRequest;
import com.claw.dto.request.RegisterRequest;
import com.claw.dto.response.LoginResponse;
import com.claw.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u8ba4\u8bc1\u7ba1\u7406")
@RestController
@RequestMapping(value={"/api/v1/auth"})
public class AuthController {
    private final AuthService authService;

    @Operation(summary="\u7528\u6237\u767b\u5f55")
    @PostMapping(value={"/login"})
    @OperationLog(value="\u7528\u6237\u767b\u5f55")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(this.authService.login(request));
    }

    @Operation(summary="\u7528\u6237\u6ce8\u518c")
    @PostMapping(value={"/register"})
    @OperationLog(value="\u7528\u6237\u6ce8\u518c")
    public Result<Long> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(this.authService.register(request));
    }

    @Operation(summary="\u5237\u65b0Token")
    @PostMapping(value={"/refresh"})
    public Result<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return Result.success(this.authService.refresh(request.getRefreshToken()));
    }

    @Operation(summary="\u767b\u51fa")
    @PostMapping(value={"/logout"})
    public Result<Void> logout() {
        return Result.success();
    }

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
}
