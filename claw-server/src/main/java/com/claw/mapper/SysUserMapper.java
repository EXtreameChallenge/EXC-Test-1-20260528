package com.claw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claw.entity.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper
extends BaseMapper<SysUser> {
    @Insert(value={"INSERT INTO sys_user_role(user_id, role_id) VALUES(#{userId}, #{roleId})"})
    public int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
