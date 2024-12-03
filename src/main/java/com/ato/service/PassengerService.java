package com.ato.service;

import com.ato.dao.dto.user.PassengerPageQueryDTO;
import com.ato.dao.result.Result;
import com.ato.dao.dto.user.PassengerDTO;

import java.util.List;

public interface PassengerService {
    Result addPassenger(PassengerDTO passengerDTO);

    Result pageQuery(PassengerPageQueryDTO passengerPageQueryDTO);

    Result deleteByIds(List<Long> ids);

    Result updatePassenger(PassengerDTO passengerDTO);
}