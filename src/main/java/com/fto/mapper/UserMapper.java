package com.fto.mapper;

import com.fto.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Select("select * from users where username = #{username}")
    User getByUsername(String username);
}
