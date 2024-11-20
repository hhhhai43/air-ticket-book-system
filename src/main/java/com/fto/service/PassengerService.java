package com.fto.service;

import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.PassengerDTO;

public interface PassengerService {
    Result addPassenger(PassengerDTO passengerDTO);
}
