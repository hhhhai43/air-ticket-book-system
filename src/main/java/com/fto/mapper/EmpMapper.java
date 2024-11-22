package com.fto.mapper;

import com.fto.pojo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 员工mapper
 */
@Mapper
public interface EmpMapper {
    /**
     * 根据姓名查找员工
     *
     * @param username
     * @return
     */
    @Select("select * from users where username = #{username}")
    Employee getByUsername(String username);
}
