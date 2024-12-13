package com.ato.service;

import com.ato.pojo.dto.emp.EmployeeLoginDTO;
import com.ato.pojo.result.Result;

public interface EmpService {
    Result login(EmployeeLoginDTO employeeLoginDTO);
}
