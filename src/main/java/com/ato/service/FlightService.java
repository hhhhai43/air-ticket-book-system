package com.ato.service;

import com.ato.dao.dto.emp.DelayDTO;
import com.ato.dao.dto.emp.FlightDTO;
import com.ato.dao.dto.user.FlightPageQueryDTO;
import com.ato.dao.result.Result;

public interface FlightService {
    Result addFlight(FlightDTO flightDTO);

    Result pageQueryFlight(FlightPageQueryDTO flightPageQueryDTO);

    Result updateDelay(DelayDTO delayDTO);

    Result getAllFlights();
}
