/*
 * Decompiled with CFR 0.152.
 */
package com.claw.common;

public enum ErrorCode {
    SUCCESS(200, "\u64cd\u4f5c\u6210\u529f"),
    BAD_REQUEST(400, "\u8bf7\u6c42\u53c2\u6570\u9519\u8bef"),
    UNAUTHORIZED(401, "\u672a\u8ba4\u8bc1\uff0c\u8bf7\u5148\u767b\u5f55"),
    FORBIDDEN(403, "\u65e0\u6743\u9650\u8bbf\u95ee"),
    NOT_FOUND(404, "\u8d44\u6e90\u4e0d\u5b58\u5728"),
    CONFLICT(409, "\u8d44\u6e90\u51b2\u7a81"),
    TOO_MANY_REQUESTS(429, "\u8bf7\u6c42\u8fc7\u4e8e\u9891\u7e41"),
    INTERNAL_ERROR(500, "\u670d\u52a1\u5668\u5185\u90e8\u9519\u8bef"),
    USERNAME_EXISTS(1001, "\u7528\u6237\u540d\u5df2\u5b58\u5728"),
    LOGIN_FAILED(1002, "\u7528\u6237\u540d\u6216\u5bc6\u7801\u9519\u8bef"),
    TOKEN_EXPIRED(1003, "Token\u5df2\u8fc7\u671f"),
    TOKEN_INVALID(1004, "Token\u65e0\u6548"),
    REFRESH_TOKEN_EXPIRED(1005, "\u5237\u65b0Token\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55"),
    VEHICLE_NOT_AVAILABLE(2001, "\u8f66\u8f86\u5f53\u524d\u4e0d\u53ef\u7528"),
    VEHICLE_IN_DELIVERY(2002, "\u8f66\u8f86\u914d\u9001\u4e2d\uff0c\u4e0d\u53ef\u64cd\u4f5c"),
    VEHICLE_FAULT(2003, "\u8f66\u8f86\u6545\u969c\u4e2d\uff0c\u4e0d\u53ef\u8c03\u5ea6"),
    TASK_STATUS_INVALID(3001, "\u4efb\u52a1\u72b6\u6001\u6d41\u8f6c\u4e0d\u5408\u6cd5"),
    TASK_CANNOT_DELETE(3002, "\u5f53\u524d\u4efb\u52a1\u72b6\u6001\u4e0d\u53ef\u5220\u9664"),
    ALERT_ALREADY_CONFIRMED(4001, "\u544a\u8b66\u5df2\u786e\u8ba4"),
    ROLE_IN_USE(5001, "\u8be5\u89d2\u8272\u4e0b\u5b58\u5728\u7528\u6237\uff0c\u4e0d\u53ef\u5220\u9664");

    private final int code;
    private final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
