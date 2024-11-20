package com.fto.service;

import com.fto.pojo.dto.user.PassengerPageQueryDTO;
import com.fto.pojo.result.PageResult;
import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.PassengerDTO;

import java.util.List;

public interface PassengerService {
    Result addPassenger(PassengerDTO passengerDTO);

    Result pageQuery(PassengerPageQueryDTO passengerPageQueryDTO);

    Result deleteByIds(List<Long> ids);

    Result updatePassenger(PassengerDTO passengerDTO);
}
