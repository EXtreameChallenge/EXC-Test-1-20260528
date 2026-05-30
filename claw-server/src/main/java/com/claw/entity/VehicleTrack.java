package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName(value="vehicle_track")
public class VehicleTrack {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String vehicleId;
    private BigDecimal battery;
    private BigDecimal speed;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal heading;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime recordedAt;

    public Long getId() {
        return this.id;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public BigDecimal getBattery() {
        return this.battery;
    }

    public BigDecimal getSpeed() {
        return this.speed;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public BigDecimal getHeading() {
        return this.heading;
    }

    public LocalDateTime getRecordedAt() {
        return this.recordedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setBattery(BigDecimal battery) {
        this.battery = battery;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setHeading(BigDecimal heading) {
        this.heading = heading;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VehicleTrack)) {
            return false;
        }
        VehicleTrack other = (VehicleTrack)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$vehicleId = this.getVehicleId();
        String other$vehicleId = other.getVehicleId();
        if (this$vehicleId == null ? other$vehicleId != null : !this$vehicleId.equals(other$vehicleId)) {
            return false;
        }
        BigDecimal this$battery = this.getBattery();
        BigDecimal other$battery = other.getBattery();
        if (this$battery == null ? other$battery != null : !((Object)this$battery).equals(other$battery)) {
            return false;
        }
        BigDecimal this$speed = this.getSpeed();
        BigDecimal other$speed = other.getSpeed();
        if (this$speed == null ? other$speed != null : !((Object)this$speed).equals(other$speed)) {
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
        BigDecimal this$heading = this.getHeading();
        BigDecimal other$heading = other.getHeading();
        if (this$heading == null ? other$heading != null : !((Object)this$heading).equals(other$heading)) {
            return false;
        }
        LocalDateTime this$recordedAt = this.getRecordedAt();
        LocalDateTime other$recordedAt = other.getRecordedAt();
        return !(this$recordedAt == null ? other$recordedAt != null : !((Object)this$recordedAt).equals(other$recordedAt));
    }

    protected boolean canEqual(Object other) {
        return other instanceof VehicleTrack;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $vehicleId = this.getVehicleId();
        result = result * 59 + ($vehicleId == null ? 43 : $vehicleId.hashCode());
        BigDecimal $battery = this.getBattery();
        result = result * 59 + ($battery == null ? 43 : ((Object)$battery).hashCode());
        BigDecimal $speed = this.getSpeed();
        result = result * 59 + ($speed == null ? 43 : ((Object)$speed).hashCode());
        BigDecimal $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : ((Object)$latitude).hashCode());
        BigDecimal $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : ((Object)$longitude).hashCode());
        BigDecimal $heading = this.getHeading();
        result = result * 59 + ($heading == null ? 43 : ((Object)$heading).hashCode());
        LocalDateTime $recordedAt = this.getRecordedAt();
        result = result * 59 + ($recordedAt == null ? 43 : ((Object)$recordedAt).hashCode());
        return result;
    }

    public String toString() {
        return "VehicleTrack(id=" + this.getId() + ", vehicleId=" + this.getVehicleId() + ", battery=" + this.getBattery() + ", speed=" + this.getSpeed() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", heading=" + this.getHeading() + ", recordedAt=" + this.getRecordedAt() + ")";
    }
}
