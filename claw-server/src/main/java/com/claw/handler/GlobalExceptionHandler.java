package com.claw.handler;

import com.claw.common.BusinessException;
import com.claw.common.ErrorCode;
import com.claw.common.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value={BusinessException.class})
    @ResponseStatus(value=HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", (Object)e.getMessage());
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        log.warn("Validation error: {}", (Object)message);
        return Result.error(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(value={ConstraintViolationException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
        log.warn("Constraint violation: {}", (Object)message);
        return Result.error(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    @ResponseStatus(value=HttpStatus.FORBIDDEN)
    public Result<Void> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", (Object)e.getMessage());
        return Result.error(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(value={NoResourceFoundException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFound(NoResourceFoundException e) {
        return Result.error(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(value={Exception.class})
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unexpected error", (Throwable)e);
        return Result.error(ErrorCode.INTERNAL_ERROR);
    }
}
