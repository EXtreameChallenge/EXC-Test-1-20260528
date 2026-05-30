package com.claw.security;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.claw.entity.SysUser;
import com.claw.mapper.SysRoleMapper;
import com.claw.mapper.SysUserMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl
implements UserDetailsService {
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = (SysUser)this.userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, (Object)username));
        if (user == null) {
            throw new UsernameNotFoundException("\u7528\u6237\u4e0d\u5b58\u5728: " + username);
        }
        if (user.getStatus() != 1) {
            throw new UsernameNotFoundException("\u7528\u6237\u5df2\u88ab\u7981\u7528: " + username);
        }
        List<String> roleKeys = this.roleMapper.selectRoleKeysByUserId(user.getId());
        List<String> permissions = this.roleMapper.selectPermissionsByUserId(user.getId());
        Set authorities = permissions.stream().map(p -> new SimpleGrantedAuthority("PERM_" + p)).collect(Collectors.toSet());
        roleKeys.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
        user.setRoleKey(roleKeys.isEmpty() ? "viewer" : roleKeys.get(0));
        user.setPermissions(Set.copyOf(permissions));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public UserDetailsServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }
}
