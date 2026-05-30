package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="dispatch_task")
public class DispatchTask {
    @TableId(type=IdType.INPUT)
    private String id;
    private String name;
    private String status;
    private String destination;
    private String cargoType;
    private String vehicleId;
    private String priority;
    private LocalDateTime executeTime;
    private LocalDateTime completedTime;
    private Long creatorId;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getCargoType() {
        return this.cargoType;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public String getPriority() {
        return this.priority;
    }

    public LocalDateTime getExecuteTime() {
        return this.executeTime;
    }

    public LocalDateTime getCompletedTime() {
        return this.completedTime;
    }

    public Long getCreatorId() {
        return this.creatorId;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
        if (!(o instanceof DispatchTask)) {
            return false;
        }
        DispatchTask other = (DispatchTask)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$creatorId = this.getCreatorId();
        Long other$creatorId = other.getCreatorId();
        if (this$creatorId == null ? other$creatorId != null : !((Object)this$creatorId).equals(other$creatorId)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$destination = this.getDestination();
        String other$destination = other.getDestination();
        if (this$destination == null ? other$destination != null : !this$destination.equals(other$destination)) {
            return false;
        }
        String this$cargoType = this.getCargoType();
        String other$cargoType = other.getCargoType();
        if (this$cargoType == null ? other$cargoType != null : !this$cargoType.equals(other$cargoType)) {
            return false;
        }
        String this$vehicleId = this.getVehicleId();
        String other$vehicleId = other.getVehicleId();
        if (this$vehicleId == null ? other$vehicleId != null : !this$vehicleId.equals(other$vehicleId)) {
            return false;
        }
        String this$priority = this.getPriority();
        String other$priority = other.getPriority();
        if (this$priority == null ? other$priority != null : !this$priority.equals(other$priority)) {
            return false;
        }
        LocalDateTime this$executeTime = this.getExecuteTime();
        LocalDateTime other$executeTime = other.getExecuteTime();
        if (this$executeTime == null ? other$executeTime != null : !((Object)this$executeTime).equals(other$executeTime)) {
            return false;
        }
        LocalDateTime this$completedTime = this.getCompletedTime();
        LocalDateTime other$completedTime = other.getCompletedTime();
        if (this$completedTime == null ? other$completedTime != null : !((Object)this$completedTime).equals(other$completedTime)) {
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
        return other instanceof DispatchTask;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $creatorId = this.getCreatorId();
        result = result * 59 + ($creatorId == null ? 43 : ((Object)$creatorId).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $destination = this.getDestination();
        result = result * 59 + ($destination == null ? 43 : $destination.hashCode());
        String $cargoType = this.getCargoType();
        result = result * 59 + ($cargoType == null ? 43 : $cargoType.hashCode());
        String $vehicleId = this.getVehicleId();
        result = result * 59 + ($vehicleId == null ? 43 : $vehicleId.hashCode());
        String $priority = this.getPriority();
        result = result * 59 + ($priority == null ? 43 : $priority.hashCode());
        LocalDateTime $executeTime = this.getExecuteTime();
        result = result * 59 + ($executeTime == null ? 43 : ((Object)$executeTime).hashCode());
        LocalDateTime $completedTime = this.getCompletedTime();
        result = result * 59 + ($completedTime == null ? 43 : ((Object)$completedTime).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    public String toString() {
        return "DispatchTask(id=" + this.getId() + ", name=" + this.getName() + ", status=" + this.getStatus() + ", destination=" + this.getDestination() + ", cargoType=" + this.getCargoType() + ", vehicleId=" + this.getVehicleId() + ", priority=" + this.getPriority() + ", executeTime=" + this.getExecuteTime() + ", completedTime=" + this.getCompletedTime() + ", creatorId=" + this.getCreatorId() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}
