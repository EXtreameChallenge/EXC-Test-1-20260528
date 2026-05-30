package com.claw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName(value="charging_station")
public class ChargingStation {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer totalSlots;
    private Integer availableSlots;
    private BigDecimal powerKw;
    private Integer queueCount;
    private Integer status;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Integer getTotalSlots() {
        return this.totalSlots;
    }

    public Integer getAvailableSlots() {
        return this.availableSlots;
    }

    public BigDecimal getPowerKw() {
        return this.powerKw;
    }

    public Integer getQueueCount() {
        return this.queueCount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void setPowerKw(BigDecimal powerKw) {
        this.powerKw = powerKw;
    }

    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChargingStation)) {
            return false;
        }
        ChargingStation other = (ChargingStation)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$totalSlots = this.getTotalSlots();
        Integer other$totalSlots = other.getTotalSlots();
        if (this$totalSlots == null ? other$totalSlots != null : !((Object)this$totalSlots).equals(other$totalSlots)) {
            return false;
        }
        Integer this$availableSlots = this.getAvailableSlots();
        Integer other$availableSlots = other.getAvailableSlots();
        if (this$availableSlots == null ? other$availableSlots != null : !((Object)this$availableSlots).equals(other$availableSlots)) {
            return false;
        }
        Integer this$queueCount = this.getQueueCount();
        Integer other$queueCount = other.getQueueCount();
        if (this$queueCount == null ? other$queueCount != null : !((Object)this$queueCount).equals(other$queueCount)) {
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
        BigDecimal this$powerKw = this.getPowerKw();
        BigDecimal other$powerKw = other.getPowerKw();
        return !(this$powerKw == null ? other$powerKw != null : !((Object)this$powerKw).equals(other$powerKw));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChargingStation;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $totalSlots = this.getTotalSlots();
        result = result * 59 + ($totalSlots == null ? 43 : ((Object)$totalSlots).hashCode());
        Integer $availableSlots = this.getAvailableSlots();
        result = result * 59 + ($availableSlots == null ? 43 : ((Object)$availableSlots).hashCode());
        Integer $queueCount = this.getQueueCount();
        result = result * 59 + ($queueCount == null ? 43 : ((Object)$queueCount).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        BigDecimal $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : ((Object)$latitude).hashCode());
        BigDecimal $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : ((Object)$longitude).hashCode());
        BigDecimal $powerKw = this.getPowerKw();
        result = result * 59 + ($powerKw == null ? 43 : ((Object)$powerKw).hashCode());
        return result;
    }

    public String toString() {
        return "ChargingStation(id=" + this.getId() + ", name=" + this.getName() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", totalSlots=" + this.getTotalSlots() + ", availableSlots=" + this.getAvailableSlots() + ", powerKw=" + this.getPowerKw() + ", queueCount=" + this.getQueueCount() + ", status=" + this.getStatus() + ")";
    }
}
