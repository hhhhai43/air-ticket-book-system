package com.ato.mapper;

import com.ato.annotation.AutoFill;
import com.ato.enumeration.OperationType;
import com.ato.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Select("select * from users where username = #{username}")
    User getByUsername(String username);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from users where id = #{userId}")
    User getByUserId(Long userId);

    /**
     * 新增用户
     * @param user
     */
    @AutoFill(value = OperationType.INSERT)
    void addUser(User user);


    /**
     * 修改用户信息
     * @param user
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateUser(User user);
}
