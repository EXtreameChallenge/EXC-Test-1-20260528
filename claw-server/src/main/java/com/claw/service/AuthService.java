package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.dto.request.LoginRequest;
import com.claw.dto.request.RegisterRequest;
import com.claw.dto.response.LoginResponse;
import com.claw.entity.SysRole;
import com.claw.entity.SysUser;
import com.claw.mapper.SysRoleMapper;
import com.claw.mapper.SysUserMapper;
import com.claw.security.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public LoginResponse login(LoginRequest request) {
        SysUser user = (SysUser)this.userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, (Object)request.getUsername()));
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "\u8d26\u53f7\u5df2\u88ab\u7981\u7528");
        }
        if (!this.passwordEncoder.matches((CharSequence)request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        user.setLastLoginAt(LocalDateTime.now());
        this.userMapper.updateById(user);
        List<String> roleKeys = this.roleMapper.selectRoleKeysByUserId(user.getId());
        List<String> permissions = this.roleMapper.selectPermissionsByUserId(user.getId());
        String roleKey = roleKeys.isEmpty() ? "viewer" : roleKeys.get(0);
        String accessToken = this.tokenProvider.generateAccessToken(user.getId(), roleKey);
        String refreshToken = this.tokenProvider.generateRefreshToken(user.getId());
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).expiresIn(this.tokenProvider.getAccessTokenExpiration()).user(LoginResponse.UserInfo.builder().id(user.getId()).username(user.getUsername()).name(user.getName()).phone(user.getPhone()).avatar(user.getAvatar()).roleKey(roleKey).permissions(Set.copyOf(permissions)).build()).build();
    }

    public Long register(RegisterRequest request) {
        Long count = this.userMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, (Object)request.getUsername()));
        if (count > 0L) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(this.passwordEncoder.encode((CharSequence)request.getPassword()));
        user.setName(request.getName() != null ? request.getName() : request.getUsername());
        user.setPhone(request.getPhone() != null ? request.getPhone() : "");
        user.setStatus(1);
        this.userMapper.insert(user);
        SysRole role = (SysRole)this.roleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, request.getRoleKey()));
        if (role == null) {
            SysRole defaultRole = (SysRole)this.roleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, "operator"));
            if (defaultRole == null) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "系统角色配置异常，请联系管理员");
            }
            this.userMapper.insertUserRole(user.getId(), defaultRole.getId());
        } else {
            this.userMapper.insertUserRole(user.getId(), role.getId());
        }
        return user.getId();
    }

    public LoginResponse refresh(String refreshToken) {
        if (!this.tokenProvider.validateToken(refreshToken) || !this.tokenProvider.isRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        Long userId = this.tokenProvider.getUserIdFromToken(refreshToken);
        SysUser user = (SysUser)this.userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        List<String> roleKeys = this.roleMapper.selectRoleKeysByUserId(userId);
        List<String> permissions = this.roleMapper.selectPermissionsByUserId(userId);
        String roleKey = roleKeys.isEmpty() ? "viewer" : roleKeys.get(0);
        String newAccessToken = this.tokenProvider.generateAccessToken(userId, roleKey);
        String newRefreshToken = this.tokenProvider.generateRefreshToken(userId);
        return LoginResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).expiresIn(this.tokenProvider.getAccessTokenExpiration()).user(LoginResponse.UserInfo.builder().id(user.getId()).username(user.getUsername()).name(user.getName()).phone(user.getPhone()).avatar(user.getAvatar()).roleKey(roleKey).permissions(Set.copyOf(permissions)).build()).build();
    }

    public AuthService(SysUserMapper userMapper, SysRoleMapper roleMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }
}
