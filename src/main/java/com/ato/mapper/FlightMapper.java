package com.ato.mapper;

import com.ato.annotation.AutoFill;
import com.ato.enumeration.OperationType;
import com.ato.dao.dto.emp.DelayDTO;
import com.ato.dao.dto.emp.FlightDTO;
import com.ato.dao.dto.user.FlightPageQueryDTO;
import com.ato.dao.entity.Flight;
import com.ato.dao.vo.FlightVO;
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

    @Select("SELECT * FROM flights WHERE flight_number = #{flightNumber} AND DATE(departure_time) = #{date}")
    Flight findByFlightNumberAndDate(String flightNumber, String date);

    @Select("select * from flights")
    List<Flight> getAllFlights();

    Flight queryByNumberAndDate(FlightDTO flightDTO);
}