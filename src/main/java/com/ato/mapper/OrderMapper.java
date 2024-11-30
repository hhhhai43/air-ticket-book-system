package com.ato.mapper;

import com.ato.annotation.AutoFill;
import com.ato.enumeration.OperationType;
import com.ato.enumeration.OrderStatus;
import com.ato.pojo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 订单 Mapper
 */
@Mapper
public interface OrderMapper {

    /**
     * 新增订单
     * @param order
     */
    @AutoFill(value = OperationType.INSERT)
    void saveOrder(Order order);

    @Update("UPDATE orders SET status = #{status} , update_time = NOW()WHERE id = #{orderId}")
    int updateOrderStatus(Long orderId, OrderStatus status);

    @Select("select * from orders where id = #{orderId}")
    Order queryById(Long orderId);

    void saveOrderPassengersBatch(List<Map<String, Object>> orderPassengerList);
}
