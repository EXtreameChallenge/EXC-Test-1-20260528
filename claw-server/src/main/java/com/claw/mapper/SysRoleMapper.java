package com.claw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claw.entity.SysRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMapper
extends BaseMapper<SysRole> {
    @Select(value={"SELECT r.role_key FROM sys_role r INNER JOIN sys_user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}"})
    public List<String> selectRoleKeysByUserId(@Param("userId") Long userId);

    @Select(value={"SELECT p.permission_key FROM sys_permission p INNER JOIN sys_role_permission rp ON p.id = rp.permission_id INNER JOIN sys_user_role ur ON rp.role_id = ur.role_id WHERE ur.user_id = #{userId}"})
    public List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
