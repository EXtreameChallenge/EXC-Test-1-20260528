package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName(value="vehicle")
public class Vehicle {
    @TableId(type=IdType.INPUT)
    private String id;
    private String name;
    private String model;
    private String type;
    private String status;
    private BigDecimal battery;
    private BigDecimal mileage;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime lastUpdate;
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

    public String getModel() {
        return this.model;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public BigDecimal getBattery() {
        return this.battery;
    }

    public BigDecimal getMileage() {
        return this.mileage;
    }

    public String getLocation() {
        return this.location;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
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

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBattery(BigDecimal battery) {
        this.battery = battery;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
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
        if (!(o instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle)o;
        if (!other.canEqual(this)) {
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
        String this$model = this.getModel();
        String other$model = other.getModel();
        if (this$model == null ? other$model != null : !this$model.equals(other$model)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        BigDecimal this$battery = this.getBattery();
        BigDecimal other$battery = other.getBattery();
        if (this$battery == null ? other$battery != null : !((Object)this$battery).equals(other$battery)) {
            return false;
        }
        BigDecimal this$mileage = this.getMileage();
        BigDecimal other$mileage = other.getMileage();
        if (this$mileage == null ? other$mileage != null : !((Object)this$mileage).equals(other$mileage)) {
            return false;
        }
        String this$location = this.getLocation();
        String other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals(other$location)) {
            return false;
        }
        BigDecimal this$latitude = this.getLatitude();
        BigDecimal other$latitude = other.getLatitude();
        if (this$latitude == null ? other$latitude != null : !((Object)this$latitude).equals(other$latitude)) {
            return false;
        }
        BigDecimal this$longitude = this.getLongitude();
        BigDecimal other$longitude = other.getLongitude();
        if (this$longitude == null ? other$longitude != null : !((Object)this$longitude).equals(other$longitude)) {
            return false;
        }
        LocalDateTime this$lastUpdate = this.getLastUpdate();
        LocalDateTime other$lastUpdate = other.getLastUpdate();
        if (this$lastUpdate == null ? other$lastUpdate != null : !((Object)this$lastUpdate).equals(other$lastUpdate)) {
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
        return other instanceof Vehicle;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $model = this.getModel();
        result = result * 59 + ($model == null ? 43 : $model.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        BigDecimal $battery = this.getBattery();
        result = result * 59 + ($battery == null ? 43 : ((Object)$battery).hashCode());
        BigDecimal $mileage = this.getMileage();
        result = result * 59 + ($mileage == null ? 43 : ((Object)$mileage).hashCode());
        String $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        BigDecimal $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : ((Object)$latitude).hashCode());
        BigDecimal $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : ((Object)$longitude).hashCode());
        LocalDateTime $lastUpdate = this.getLastUpdate();
        result = result * 59 + ($lastUpdate == null ? 43 : ((Object)$lastUpdate).hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    public String toString() {
        return "Vehicle(id=" + this.getId() + ", name=" + this.getName() + ", model=" + this.getModel() + ", type=" + this.getType() + ", status=" + this.getStatus() + ", battery=" + this.getBattery() + ", mileage=" + this.getMileage() + ", location=" + this.getLocation() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", lastUpdate=" + this.getLastUpdate() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}
