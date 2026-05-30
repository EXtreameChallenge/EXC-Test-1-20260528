package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="sys_operation_log")
public class SysOperationLog {
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String params;
    private String ip;
    private Integer duration;
    private Byte status;
    private String errorMsg;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getOperation() {
        return this.operation;
    }

    public String getMethod() {
        return this.method;
    }

    public String getParams() {
        return this.params;
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Byte getStatus() {
        return this.status;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysOperationLog)) {
            return false;
        }
        SysOperationLog other = (SysOperationLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$duration = this.getDuration();
        Integer other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !((Object)this$duration).equals(other$duration)) {
            return false;
        }
        Byte this$status = this.getStatus();
        Byte other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$operation = this.getOperation();
        String other$operation = other.getOperation();
        if (this$operation == null ? other$operation != null : !this$operation.equals(other$operation)) {
            return false;
        }
        String this$method = this.getMethod();
        String other$method = other.getMethod();
        if (this$method == null ? other$method != null : !this$method.equals(other$method)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        String this$ip = this.getIp();
        String other$ip = other.getIp();
        if (this$ip == null ? other$ip != null : !this$ip.equals(other$ip)) {
            return false;
        }
        String this$errorMsg = this.getErrorMsg();
        String other$errorMsg = other.getErrorMsg();
        if (this$errorMsg == null ? other$errorMsg != null : !this$errorMsg.equals(other$errorMsg)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysOperationLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $duration = this.getDuration();
        result = result * 59 + ($duration == null ? 43 : ((Object)$duration).hashCode());
        Byte $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $operation = this.getOperation();
        result = result * 59 + ($operation == null ? 43 : $operation.hashCode());
        String $method = this.getMethod();
        result = result * 59 + ($method == null ? 43 : $method.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        String $ip = this.getIp();
        result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
        String $errorMsg = this.getErrorMsg();
        result = result * 59 + ($errorMsg == null ? 43 : $errorMsg.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    public String toString() {
        return "SysOperationLog(id=" + this.getId() + ", userId=" + this.getUserId() + ", username=" + this.getUsername() + ", operation=" + this.getOperation() + ", method=" + this.getMethod() + ", params=" + this.getParams() + ", ip=" + this.getIp() + ", duration=" + this.getDuration() + ", status=" + this.getStatus() + ", errorMsg=" + this.getErrorMsg() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
