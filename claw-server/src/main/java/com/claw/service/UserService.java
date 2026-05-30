package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.entity.SysUser;
import com.claw.mapper.SysRoleMapper;
import com.claw.mapper.SysUserMapper;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(SysUserMapper userMapper, SysRoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public SysUser getUserById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在: " + id);
        }
        user.setPassword(null);
        List<String> roleKeys = roleMapper.selectRoleKeysByUserId(id);
        List<String> permissions = roleMapper.selectPermissionsByUserId(id);
        user.setRoleKey(roleKeys.isEmpty() ? "viewer" : roleKeys.get(0));
        user.setPermissions(Set.copyOf(permissions));
        return user;
    }

    public List<SysUser> getAllUsers() {
        List<SysUser> users = userMapper.selectList(null);
        for (SysUser user : users) {
            user.setPassword(null);
            List<String> roleKeys = roleMapper.selectRoleKeysByUserId(user.getId());
            user.setRoleKey(roleKeys.isEmpty() ? "viewer" : roleKeys.get(0));
        }
        return users;
    }

    @Transactional
    public SysUser createUser(SysUser user, Long roleId) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, user.getUsername()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        if (roleId != null) {
            userMapper.insertUserRole(user.getId(), roleId);
        }
        user.setPassword(null);
        return user;
    }

    @Transactional
    public SysUser updateUser(Long id, SysUser user) {
        SysUser existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在: " + id);
        }
        if (user.getName() != null) existing.setName(user.getName());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getPhone() != null) existing.setPhone(user.getPhone());
        if (user.getStatus() != null) existing.setStatus(user.getStatus());
        if (user.getAvatar() != null) existing.setAvatar(user.getAvatar());
        userMapper.updateById(existing);
        existing.setPassword(null);
        return existing;
    }

    @Transactional
    public void deleteUser(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在: " + id);
        }
        userMapper.deleteById(id);
    }
}
