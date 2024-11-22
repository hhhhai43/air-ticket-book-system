package com.ato.service;

import com.ato.pojo.dto.emp.FlightDTO;
import com.ato.pojo.dto.emp.FlightPageQueryDTO;
import com.ato.pojo.result.Result;

public interface FlightService {
    Result addFlight(FlightDTO flightDTO);

    Result pageQueryFlight(FlightPageQueryDTO flightPageQueryDTO);
}
