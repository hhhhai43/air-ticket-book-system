package com.ato.mapper;

import com.ato.annotation.AutoFill;
import com.ato.enumeration.OperationType;
import com.ato.pojo.dto.emp.DelayDTO;
import com.ato.pojo.dto.emp.FlightDTO;
import com.ato.pojo.dto.user.FlightPageQueryDTO;
import com.ato.pojo.entity.Flight;
import com.ato.pojo.vo.FlightVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 航班Mapper
 */
@Mapper
public interface FlightMapper {
    @AutoFill(value = OperationType.INSERT)
    void addFlight(Flight flight);

    Page<FlightVO> pageQuery(FlightPageQueryDTO flightPageQueryDTO);

    /**
     * 查询航班
     * @param flightDTO
     * @return
     */
    List<Flight> query(FlightDTO flightDTO);

    void updateDelay(DelayDTO delayDTO);

    @Select("select * from flights where id = #{id}")
    Flight queryById(Long flightId);
}