package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="fault_alert")
public class FaultAlert {
    @TableId(type=IdType.INPUT)
    private String id;
    private String level;
    private String message;
    private String vehicleId;
    private String status;
    private Long confirmedBy;
    private LocalDateTime confirmedAt;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;

    public String getId() {
        return this.id;
    }

    public String getLevel() {
        return this.level;
    }

    public String getMessage() {
        return this.message;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public String getStatus() {
        return this.status;
    }

    public Long getConfirmedBy() {
        return this.confirmedBy;
    }

    public LocalDateTime getConfirmedAt() {
        return this.confirmedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setConfirmedBy(Long confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FaultAlert)) {
            return false;
        }
        FaultAlert other = (FaultAlert)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$confirmedBy = this.getConfirmedBy();
        Long other$confirmedBy = other.getConfirmedBy();
        if (this$confirmedBy == null ? other$confirmedBy != null : !((Object)this$confirmedBy).equals(other$confirmedBy)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$level = this.getLevel();
        String other$level = other.getLevel();
        if (this$level == null ? other$level != null : !this$level.equals(other$level)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        String this$vehicleId = this.getVehicleId();
        String other$vehicleId = other.getVehicleId();
        if (this$vehicleId == null ? other$vehicleId != null : !this$vehicleId.equals(other$vehicleId)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDateTime this$confirmedAt = this.getConfirmedAt();
        LocalDateTime other$confirmedAt = other.getConfirmedAt();
        if (this$confirmedAt == null ? other$confirmedAt != null : !((Object)this$confirmedAt).equals(other$confirmedAt)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FaultAlert;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $confirmedBy = this.getConfirmedBy();
        result = result * 59 + ($confirmedBy == null ? 43 : ((Object)$confirmedBy).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : $level.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        String $vehicleId = this.getVehicleId();
        result = result * 59 + ($vehicleId == null ? 43 : $vehicleId.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDateTime $confirmedAt = this.getConfirmedAt();
        result = result * 59 + ($confirmedAt == null ? 43 : ((Object)$confirmedAt).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    public String toString() {
        return "FaultAlert(id=" + this.getId() + ", level=" + this.getLevel() + ", message=" + this.getMessage() + ", vehicleId=" + this.getVehicleId() + ", status=" + this.getStatus() + ", confirmedBy=" + this.getConfirmedBy() + ", confirmedAt=" + this.getConfirmedAt() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
