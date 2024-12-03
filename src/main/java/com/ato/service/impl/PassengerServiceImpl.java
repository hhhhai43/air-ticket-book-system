package com.ato.service.impl;

import com.ato.constant.MessageConstant;
import com.ato.context.BaseContext;
import com.ato.mapper.PassengerMapper;
import com.ato.dao.dto.user.PassengerPageQueryDTO;
import com.ato.dao.result.PageResult;
import com.ato.dao.result.Result;
import com.ato.dao.dto.user.PassengerDTO;
import com.ato.dao.entity.Passenger;
import com.ato.dao.vo.PassengerVO;
import com.ato.service.PassengerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 乘客服务类
 */
@Slf4j
@Service
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    private PassengerMapper passengerMapper;

    /**
     * 添加乘客信息
     * @param passengerDTO
     * @return
     */
    @Override
    public Result addPassenger(PassengerDTO passengerDTO) {
        Long userId = BaseContext.getCurrentId();

        // 判断是否已存在乘客数据
        Passenger passenger = passengerMapper.getPassengerByIdNumber(passengerDTO.getIdNumber());
        //不存在则添加
        if(passenger == null){
            passenger = new Passenger();
            BeanUtils.copyProperties(passengerDTO, passenger);
            passengerMapper.addPassenger(passenger);
        }else {
            //存在则检查身份证号和姓名是否与数据库中匹配
            if(!Objects.equals(passenger.getName(), passengerDTO.getName())){
                return Result.error(MessageConstant.ID_NAME_ERROR);
            }
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

    /**
     * 分页查询乘客
     * @param passengerPageQueryDTO
     * @return
     */
    @Override
    public Result pageQuery(PassengerPageQueryDTO passengerPageQueryDTO) {
        //select * from passenger limit 0,10
        PageHelper.startPage(passengerPageQueryDTO.getPage(),passengerPageQueryDTO.getPageSize());
        Page<PassengerVO> page= passengerMapper.pageQueryWithPhone(passengerPageQueryDTO);
        log.info("{}",page);

        long total = page.getTotal();
        List<PassengerVO> records = page.getResult();
        PageResult pageResult = new PageResult(total, records);
        return Result.success(pageResult);
    }

    /**
     * 批量删除乘客
     * @param ids
     * @return
     */
    @Override
    public Result deleteByIds(List<Long> ids) {
        log.info("请求删除乘客id：{}", ids);
        Long userId = BaseContext.getCurrentId();
        passengerMapper.deleteByIds(userId, ids);
        return Result.success();
    }

    /**
     * 更新乘客信息
     * @param passengerDTO
     * @return
     */
    @Override
    public Result updatePassenger(PassengerDTO passengerDTO) {
        Long userId = BaseContext.getCurrentId();
        // 根据身份证号获取乘客信息
        Passenger passenger = passengerMapper.getPassengerByIdNumber(passengerDTO.getIdNumber());
        // 不存在乘客则添加乘客
        if(passenger == null){
            return Result.error(MessageConstant.ID_NOT_FOUND);
        }
        // 校验身份证号与姓名是否正确
        if(!Objects.equals(passenger.getName(), passengerDTO.getName())){
            return Result.error(MessageConstant.ID_NAME_ERROR);
        }
        // 获取乘客id
        Long passengerId = passenger.getId();
        passengerMapper.updatePassenger(passengerId, userId, passengerDTO);
        return Result.success();
    }
}