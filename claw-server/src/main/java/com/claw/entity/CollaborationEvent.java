package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="collaboration_event")
public class CollaborationEvent {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String eventType;
    private Long sourceUserId;
    private Long targetUserId;
    private String sourceDevice;
    private String payload;
    private LocalDateTime readAt;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() {
        return this.id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public Long getSourceUserId() {
        return this.sourceUserId;
    }

    public Long getTargetUserId() {
        return this.targetUserId;
    }

    public String getSourceDevice() {
        return this.sourceDevice;
    }

    public String getPayload() {
        return this.payload;
    }

    public LocalDateTime getReadAt() {
        return this.readAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setSourceDevice(String sourceDevice) {
        this.sourceDevice = sourceDevice;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CollaborationEvent)) {
            return false;
        }
        CollaborationEvent other = (CollaborationEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$sourceUserId = this.getSourceUserId();
        Long other$sourceUserId = other.getSourceUserId();
        if (this$sourceUserId == null ? other$sourceUserId != null : !((Object)this$sourceUserId).equals(other$sourceUserId)) {
            return false;
        }
        Long this$targetUserId = this.getTargetUserId();
        Long other$targetUserId = other.getTargetUserId();
        if (this$targetUserId == null ? other$targetUserId != null : !((Object)this$targetUserId).equals(other$targetUserId)) {
            return false;
        }
        String this$eventType = this.getEventType();
        String other$eventType = other.getEventType();
        if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) {
            return false;
        }
        String this$sourceDevice = this.getSourceDevice();
        String other$sourceDevice = other.getSourceDevice();
        if (this$sourceDevice == null ? other$sourceDevice != null : !this$sourceDevice.equals(other$sourceDevice)) {
            return false;
        }
        String this$payload = this.getPayload();
        String other$payload = other.getPayload();
        if (this$payload == null ? other$payload != null : !this$payload.equals(other$payload)) {
            return false;
        }
        LocalDateTime this$readAt = this.getReadAt();
        LocalDateTime other$readAt = other.getReadAt();
        if (this$readAt == null ? other$readAt != null : !((Object)this$readAt).equals(other$readAt)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CollaborationEvent;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $sourceUserId = this.getSourceUserId();
        result = result * 59 + ($sourceUserId == null ? 43 : ((Object)$sourceUserId).hashCode());
        Long $targetUserId = this.getTargetUserId();
        result = result * 59 + ($targetUserId == null ? 43 : ((Object)$targetUserId).hashCode());
        String $eventType = this.getEventType();
        result = result * 59 + ($eventType == null ? 43 : $eventType.hashCode());
        String $sourceDevice = this.getSourceDevice();
        result = result * 59 + ($sourceDevice == null ? 43 : $sourceDevice.hashCode());
        String $payload = this.getPayload();
        result = result * 59 + ($payload == null ? 43 : $payload.hashCode());
        LocalDateTime $readAt = this.getReadAt();
        result = result * 59 + ($readAt == null ? 43 : ((Object)$readAt).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    public String toString() {
        return "CollaborationEvent(id=" + this.getId() + ", eventType=" + this.getEventType() + ", sourceUserId=" + this.getSourceUserId() + ", targetUserId=" + this.getTargetUserId() + ", sourceDevice=" + this.getSourceDevice() + ", payload=" + this.getPayload() + ", readAt=" + this.getReadAt() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
