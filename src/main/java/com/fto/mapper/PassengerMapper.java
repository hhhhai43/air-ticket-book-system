package com.fto.mapper;

import com.fto.annotation.AutoFill;
import com.fto.enumeration.OperationType;
import com.fto.pojo.entity.Passenger;
import com.fto.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PassengerMapper {
    /**
     * 根据身份证号获取乘客
     * @param idNumber
     * @return
     */
    @Select("select * from passenger where id_number = #{idNumber}")
    Passenger getByIdNumber(String idNumber);

    /**
     * 添加乘客
     * @param passenger
     */
    void addPassenger(Passenger passenger);

    /**
     * 关联用户和乘客
     * @param userId
     * @param passengerId
     */
    void addPassengerToUser(Long userId, Long passengerId, String phonenumber);

    boolean getAssociation(Long userId, Long passengerId);
}
