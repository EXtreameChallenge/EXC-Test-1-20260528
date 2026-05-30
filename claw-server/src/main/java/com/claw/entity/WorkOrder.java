package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="work_order")
public class WorkOrder {
    @TableId(type=IdType.INPUT)
    private String id;
    private String title;
    private String status;
    private String priority;
    private String vehicleId;
    private String alertId;
    private Long assigneeId;
    private String description;
    private LocalDateTime completedAt;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStatus() {
        return this.status;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public String getAlertId() {
        return this.alertId;
    }

    public Long getAssigneeId() {
        return this.assigneeId;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorkOrder)) {
            return false;
        }
        WorkOrder other = (WorkOrder)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$assigneeId = this.getAssigneeId();
        Long other$assigneeId = other.getAssigneeId();
        if (this$assigneeId == null ? other$assigneeId != null : !((Object)this$assigneeId).equals(other$assigneeId)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$priority = this.getPriority();
        String other$priority = other.getPriority();
        if (this$priority == null ? other$priority != null : !this$priority.equals(other$priority)) {
            return false;
        }
        String this$vehicleId = this.getVehicleId();
        String other$vehicleId = other.getVehicleId();
        if (this$vehicleId == null ? other$vehicleId != null : !this$vehicleId.equals(other$vehicleId)) {
            return false;
        }
        String this$alertId = this.getAlertId();
        String other$alertId = other.getAlertId();
        if (this$alertId == null ? other$alertId != null : !this$alertId.equals(other$alertId)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        LocalDateTime this$completedAt = this.getCompletedAt();
        LocalDateTime other$completedAt = other.getCompletedAt();
        if (this$completedAt == null ? other$completedAt != null : !((Object)this$completedAt).equals(other$completedAt)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$updatedAt = this.getUpdatedAt();
        LocalDateTime other$updatedAt = other.getUpdatedAt();
        return !(this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorkOrder;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $assigneeId = this.getAssigneeId();
        result = result * 59 + ($assigneeId == null ? 43 : ((Object)$assigneeId).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $priority = this.getPriority();
        result = result * 59 + ($priority == null ? 43 : $priority.hashCode());
        String $vehicleId = this.getVehicleId();
        result = result * 59 + ($vehicleId == null ? 43 : $vehicleId.hashCode());
        String $alertId = this.getAlertId();
        result = result * 59 + ($alertId == null ? 43 : $alertId.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        LocalDateTime $completedAt = this.getCompletedAt();
        result = result * 59 + ($completedAt == null ? 43 : ((Object)$completedAt).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    public String toString() {
        return "WorkOrder(id=" + this.getId() + ", title=" + this.getTitle() + ", status=" + this.getStatus() + ", priority=" + this.getPriority() + ", vehicleId=" + this.getVehicleId() + ", alertId=" + this.getAlertId() + ", assigneeId=" + this.getAssigneeId() + ", description=" + this.getDescription() + ", completedAt=" + this.getCompletedAt() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}
