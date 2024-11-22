package com.fto.mapper;

import com.fto.annotation.AutoFill;
import com.fto.enumeration.OperationType;
import com.fto.pojo.entity.Flight;
import org.apache.ibatis.annotations.Mapper;

/**
 * 航班Mapper
 */
@Mapper
public interface FlightMapper {
    @AutoFill(value = OperationType.INSERT)
    void addFlight(Flight flight);
}