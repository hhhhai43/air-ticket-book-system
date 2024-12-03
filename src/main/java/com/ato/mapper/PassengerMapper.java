package com.ato.mapper;

import com.ato.dao.dto.user.PassengerDTO;
import com.ato.dao.dto.user.PassengerPageQueryDTO;
import com.ato.dao.entity.Passenger;
import com.ato.dao.vo.OrderDetailPassengerVO;
import com.ato.dao.vo.PassengerVO;
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

    /**
     * 根据身份证号查询乘客
     * @param idNumber
     * @return
     */
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
    Page<PassengerVO> pageQueryWithPhone(PassengerPageQueryDTO passengerPageQueryDTO);

    /**
     * 根据id批量删除乘客与用户关联信息
     * @param userId
     * @param ids
     */
    void deleteByIds(Long userId, List<Long> ids);

    /**
     * 更新乘客信息
     *
     * @param passengerId
     * @param userId
     * @param passengerDTO
     */
    void updatePassenger(Long passengerId, Long userId, PassengerDTO passengerDTO);


    /**
     * 根据id查询乘客信息
     * @param ids
     * @return
     */
    List<OrderDetailPassengerVO> getByIds(List<Long> ids);

    /**
     *
     根据订单 ID 获取乘客 ID 列表
      */
    @Select("SELECT passenger_id FROM order_passenger WHERE order_id = #{orderId}")
    List<Long> getPassengerIdsByOrderId(Long orderId);

    /**
     * 获取乘客DTO信息
     * @param orderId
     * @param userId
     * @return
     */
    List<PassengerDTO> getPassengerDetailsByOrderIdAndUserId(Long orderId, Long userId);
}
