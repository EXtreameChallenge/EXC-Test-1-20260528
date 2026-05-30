package com.claw.security;

import com.claw.common.ErrorCode;
import com.claw.common.Result;
import com.claw.mapper.SysRoleMapper;
import com.claw.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter
extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtTokenProvider tokenProvider;
    private final SysRoleMapper roleMapper;
    private final ObjectMapper objectMapper;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveToken(request);
        if (StringUtils.hasText((String)token) && this.tokenProvider.validateToken(token)) {
            try {
                Long userId = this.tokenProvider.getUserIdFromToken(token);
                String roleKey = this.tokenProvider.getRoleFromToken(token);
                HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleKey));
                if ("admin".equals(roleKey)) {
                    authorities.add(new SimpleGrantedAuthority("PERM_fleet:manage"));
                    authorities.add(new SimpleGrantedAuthority("PERM_task:dispatch"));
                    authorities.add(new SimpleGrantedAuthority("PERM_energy:manage"));
                    authorities.add(new SimpleGrantedAuthority("PERM_analytics:view"));
                    authorities.add(new SimpleGrantedAuthority("PERM_ai:chat"));
                    authorities.add(new SimpleGrantedAuthority("PERM_fault:manage"));
                    authorities.add(new SimpleGrantedAuthority("PERM_vehicle:manage"));
                    authorities.add(new SimpleGrantedAuthority("PERM_system:settings"));
                } else {
                    try {
                        List<String> permissions = this.roleMapper.selectPermissionsByUserId(userId);
                        for (String perm : permissions) {
                            authorities.add(new SimpleGrantedAuthority("PERM_" + perm));
                        }
                    }
                    catch (Exception e) {
                        log.warn("Query permissions failed for userId={}, using role-based fallback", userId, (Object)e);
                        this.addRoleBasedPermissions(roleKey, authorities);
                    }
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication((Authentication)authentication);
            }
            catch (Exception e) {
                this.sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
                return;
            }
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    private void addRoleBasedPermissions(String roleKey, Set<SimpleGrantedAuthority> authorities) {
        switch (roleKey) {
            case "operator": {
                authorities.add(new SimpleGrantedAuthority("PERM_fleet:manage"));
                authorities.add(new SimpleGrantedAuthority("PERM_task:dispatch"));
                authorities.add(new SimpleGrantedAuthority("PERM_energy:manage"));
                authorities.add(new SimpleGrantedAuthority("PERM_analytics:view"));
                authorities.add(new SimpleGrantedAuthority("PERM_ai:chat"));
                authorities.add(new SimpleGrantedAuthority("PERM_fault:manage"));
                authorities.add(new SimpleGrantedAuthority("PERM_vehicle:manage"));
                break;
            }
            case "driver": {
                authorities.add(new SimpleGrantedAuthority("PERM_vehicle:manage"));
                authorities.add(new SimpleGrantedAuthority("PERM_analytics:view"));
                authorities.add(new SimpleGrantedAuthority("PERM_ai:chat"));
                break;
            }
            case "viewer": {
                authorities.add(new SimpleGrantedAuthority("PERM_analytics:view"));
                authorities.add(new SimpleGrantedAuthority("PERM_fleet:manage"));
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText((String)bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getCode() == 401 ? 401 : 403);
        Result result = Result.error(errorCode);
        response.getWriter().write(this.objectMapper.writeValueAsString(result));
    }

    public JwtAuthFilter(JwtTokenProvider tokenProvider, SysRoleMapper roleMapper, ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.roleMapper = roleMapper;
        this.objectMapper = objectMapper;
    }
}
