package com.claw.aspect;

import com.claw.aspect.OperationLog;
import com.claw.entity.SysOperationLog;
import com.claw.mapper.SysOperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class OperationLogAspect {
    private final SysOperationLogMapper logMapper;

    @Around(value="@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        Object object;
        long startTime = System.currentTimeMillis();
        SysOperationLog log = new SysOperationLog();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && (object = auth.getPrincipal()) instanceof Long) {
                Long userId = (Long)object;
                log.setUserId(userId);
            }
        }
        catch (Exception auth) {
            // empty catch block
        }
        MethodSignature signature = (MethodSignature)point.getSignature();
        log.setOperation(operationLog.value().isEmpty() ? signature.getName() : operationLog.value());
        log.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                log.setIp(request.getRemoteAddr());
            }
        }
        catch (Exception attrs) {
            // empty catch block
        }
        try {
            Object result = point.proceed();
            log.setStatus((byte)1);
            object = result;
            return object;
        }
        catch (Throwable e) {
            log.setStatus((byte)0);
            log.setErrorMsg(e.getMessage());
            throw e;
        }
        finally {
            log.setDuration((int)(System.currentTimeMillis() - startTime));
            try {
                this.logMapper.insert(log);
            }
            catch (Exception exception) {}
        }
    }

    public OperationLogAspect(SysOperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }
}
