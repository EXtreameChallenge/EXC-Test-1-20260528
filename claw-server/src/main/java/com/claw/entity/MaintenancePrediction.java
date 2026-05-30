package com.claw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value="maintenance_prediction")
public class MaintenancePrediction {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String vehicleId;
    private String component;
    private BigDecimal healthScore;
    private LocalDate predictedFailureDate;
    private String recommendation;
    private String status;
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    public Long getId() {
        return this.id;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public String getComponent() {
        return this.component;
    }

    public BigDecimal getHealthScore() {
        return this.healthScore;
    }

    public LocalDate getPredictedFailureDate() {
        return this.predictedFailureDate;
    }

    public String getRecommendation() {
        return this.recommendation;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setHealthScore(BigDecimal healthScore) {
        this.healthScore = healthScore;
    }

    public void setPredictedFailureDate(LocalDate predictedFailureDate) {
        this.predictedFailureDate = predictedFailureDate;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(o instanceof MaintenancePrediction)) {
            return false;
        }
        MaintenancePrediction other = (MaintenancePrediction)o;
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
        String this$component = this.getComponent();
        String other$component = other.getComponent();
        if (this$component == null ? other$component != null : !this$component.equals(other$component)) {
            return false;
        }
        BigDecimal this$healthScore = this.getHealthScore();
        BigDecimal other$healthScore = other.getHealthScore();
        if (this$healthScore == null ? other$healthScore != null : !((Object)this$healthScore).equals(other$healthScore)) {
            return false;
        }
        LocalDate this$predictedFailureDate = this.getPredictedFailureDate();
        LocalDate other$predictedFailureDate = other.getPredictedFailureDate();
        if (this$predictedFailureDate == null ? other$predictedFailureDate != null : !((Object)this$predictedFailureDate).equals(other$predictedFailureDate)) {
            return false;
        }
        String this$recommendation = this.getRecommendation();
        String other$recommendation = other.getRecommendation();
        if (this$recommendation == null ? other$recommendation != null : !this$recommendation.equals(other$recommendation)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
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
        return other instanceof MaintenancePrediction;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $vehicleId = this.getVehicleId();
        result = result * 59 + ($vehicleId == null ? 43 : $vehicleId.hashCode());
        String $component = this.getComponent();
        result = result * 59 + ($component == null ? 43 : $component.hashCode());
        BigDecimal $healthScore = this.getHealthScore();
        result = result * 59 + ($healthScore == null ? 43 : ((Object)$healthScore).hashCode());
        LocalDate $predictedFailureDate = this.getPredictedFailureDate();
        result = result * 59 + ($predictedFailureDate == null ? 43 : ((Object)$predictedFailureDate).hashCode());
        String $recommendation = this.getRecommendation();
        result = result * 59 + ($recommendation == null ? 43 : $recommendation.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    public String toString() {
        return "MaintenancePrediction(id=" + this.getId() + ", vehicleId=" + this.getVehicleId() + ", component=" + this.getComponent() + ", healthScore=" + this.getHealthScore() + ", predictedFailureDate=" + this.getPredictedFailureDate() + ", recommendation=" + this.getRecommendation() + ", status=" + this.getStatus() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}
