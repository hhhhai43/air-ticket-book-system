package com.fto.service;

import com.fto.pojo.dto.emp.FlightDTO;
import com.fto.pojo.result.Result;

public interface FlightService {
    Result addFlight(FlightDTO flightDTO);
}
