package com.ato.service;

import com.ato.pojo.dto.user.PassengerPageQueryDTO;
import com.ato.pojo.result.Result;
import com.ato.pojo.dto.user.PassengerDTO;

import java.util.List;

public interface PassengerService {
    Result addPassenger(PassengerDTO passengerDTO);

    Result pageQuery(PassengerPageQueryDTO passengerPageQueryDTO);

    Result deleteByIds(List<Long> ids);

    Result updatePassenger(PassengerDTO passengerDTO);
}