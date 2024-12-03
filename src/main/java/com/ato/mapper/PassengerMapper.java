package com.ato.mapper;

import com.ato.pojo.dto.user.PassengerDTO;
import com.ato.pojo.dto.user.PassengerPageQueryDTO;
import com.ato.pojo.entity.Passenger;
import com.ato.pojo.vo.OrderDetailPassengerVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 乘客Mapper类
 */
@Mapper
public interface PassengerMapper {
    /**
     * 根据身份证号获取乘客
     * @param idNumber
     * @return
     */
    @Select("select * from passenger where id_number = #{idNumber}")
    Passenger getPassengerByIdNumber(String idNumber);

    /**
     * 添加乘客
     * @param passenger
     */
    void addPassenger(Passenger passenger);

    @Select("select id from passenger where id_number = #{idNumber}")
    Long getIdByIdNumber(String idNumber);

    /**
     * 关联用户和乘客
     * @param userId
     * @param passengerId
     */
    void addPassengerToUser(Long userId, Long passengerId, String phonenumber);

    /**
     * 查询用户和乘客是否关联
     * @param userId
     * @param passengerId
     * @return
     */
    boolean getAssociation(Long userId, Long passengerId);

    /**
     * 分页查询乘客信息及电话号码
     * @param passengerPageQueryDTO
     * @return
     */
    Page<com.ato.pojo.vo.PassengerVO> pageQueryWithPhone(PassengerPageQueryDTO passengerPageQueryDTO);

    /**
     * 根据id批量删除乘客与用户关联信息
     * @param userId
     * @param ids
     */
    void deleteByIds(Long userId, List<Long> ids);

    /**
     * 更新乘客信息
     * @param passengerId
     * @param passengerDTO
     */
    void updatePassenger(Long passengerId, PassengerDTO passengerDTO);


    List<OrderDetailPassengerVO> getByIds(List<Long> ids);
}
