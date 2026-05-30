package com.claw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.claw.entity.Vehicle;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VehicleMapper
extends BaseMapper<Vehicle> {
    List<Map<String, Object>> selectVehicleDispatchSummary();
}
