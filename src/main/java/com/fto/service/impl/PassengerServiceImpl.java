package com.fto.service.impl;

import com.fto.constant.MessageConstant;
import com.fto.context.BaseContext;
import com.fto.mapper.PassengerMapper;
import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.PassengerDTO;
import com.fto.pojo.entity.Passenger;
import com.fto.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 乘客服务类
 */
@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    private PassengerMapper passengerMapper;

    @Override
    public Result addPassenger(PassengerDTO passengerDTO) {
        // 检查请求id与当前用户id是否一致
        Long userId = passengerDTO.getUserId();
        if(!Objects.equals(userId, BaseContext.getCurrentId())){
            return Result.error(MessageConstant.REQUEST_ERROR);
        }

        //判断是否已存在乘客数据
        Passenger passenger = passengerMapper.getByIdNumber(passengerDTO.getIdNumber());
        //不存在则添加
        if(passenger == null){
            passenger = new Passenger();
            BeanUtils.copyProperties(passengerDTO, passenger);
            passengerMapper.addPassenger(passenger);
        }
        // 如果用户已存在乘客信息则返回错误信息
        Long passengerId = passenger.getId();//获取乘客id
        if(passengerMapper.getAssociation(userId, passengerId)){
            return Result.error(MessageConstant.PASSENGER_ALREADY_EXIST);
        }
        // 关联乘客和用户
        passengerMapper.addPassengerToUser(userId, passengerId, passengerDTO.getPhonenumber());
        return Result.success();
    }
}
