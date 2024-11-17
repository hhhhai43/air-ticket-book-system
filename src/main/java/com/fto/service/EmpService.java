package com.fto.service;

import com.fto.pojo.dto.EmployeeLoginDTO;
import com.fto.pojo.dto.Result;
import com.fto.pojo.entity.Employee;
import org.springframework.stereotype.Service;

public interface EmpService {
    Result login(EmployeeLoginDTO employeeLoginDTO);
}
