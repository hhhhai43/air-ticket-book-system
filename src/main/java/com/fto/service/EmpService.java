package com.fto.service;

import com.fto.pojo.dto.emp.EmployeeLoginDTO;
import com.fto.pojo.result.Result;

public interface EmpService {
    Result login(EmployeeLoginDTO employeeLoginDTO);
}
