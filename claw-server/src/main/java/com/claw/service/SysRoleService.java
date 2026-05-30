package com.claw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.PageResult;
import com.claw.entity.SysRole;
import com.claw.mapper.SysRoleMapper;
import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SysRoleService {
    private final SysRoleMapper roleMapper;

    public SysRoleService(SysRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public PageResult<SysRole> listRoles(int page, int size, String keyword) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(SysRole::getRoleKey, keyword)
                    .or().like(SysRole::getRoleName, keyword)
                    .or().like(SysRole::getDescription, keyword));
        }
        wrapper.orderByDesc(SysRole::getCreatedAt);
        Page<SysRole> pageResult = this.roleMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    public SysRole getRoleById(Long id) {
        SysRole role = this.roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在: " + id);
        }
        return role;
    }

    @Transactional
    public SysRole createRole(SysRole role) {
        Long count = this.roleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleKey, role.getRoleKey()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "角色标识已存在: " + role.getRoleKey());
        }
        this.roleMapper.insert(role);
        return role;
    }

    @Transactional
    public SysRole updateRole(Long id, SysRole role) {
        SysRole existing = this.getRoleById(id);
        if (role.getRoleName() != null) {
            existing.setRoleName(role.getRoleName());
        }
        if (role.getDescription() != null) {
            existing.setDescription(role.getDescription());
        }
        if (role.getStatus() != null) {
            existing.setStatus(role.getStatus());
        }
        this.roleMapper.updateById(existing);
        return existing;
    }

    @Transactional
    public void deleteRole(Long id) {
        this.getRoleById(id);
        Long userCount = this.roleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>()
                        .apply("id IN (SELECT role_id FROM sys_user_role WHERE role_id = {0})", id));
        if (userCount > 0) {
            throw new BusinessException(ErrorCode.ROLE_IN_USE);
        }
        this.roleMapper.deleteById(id);
    }
}
