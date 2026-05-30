package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName(value="geofence")
public class Geofence {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String name;
    private String type;
    private String boundary;
    private String rules;
    private Integer status;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getBoundary() {
        return this.boundary;
    }

    public String getRules() {
        return this.rules;
    }

    public Integer getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Geofence)) {
            return false;
        }
        Geofence other = (Geofence)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$boundary = this.getBoundary();
        String other$boundary = other.getBoundary();
        if (this$boundary == null ? other$boundary != null : !this$boundary.equals(other$boundary)) {
            return false;
        }
        String this$rules = this.getRules();
        String other$rules = other.getRules();
        if (this$rules == null ? other$rules != null : !this$rules.equals(other$rules)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Geofence;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $boundary = this.getBoundary();
        result = result * 59 + ($boundary == null ? 43 : $boundary.hashCode());
        String $rules = this.getRules();
        result = result * 59 + ($rules == null ? 43 : $rules.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    public String toString() {
        return "Geofence(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", boundary=" + this.getBoundary() + ", rules=" + this.getRules() + ", status=" + this.getStatus() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
