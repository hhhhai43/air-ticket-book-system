package com.ato.service;

import com.ato.dao.dto.emp.EmployeeLoginDTO;
import com.ato.dao.result.Result;

public interface EmpService {
    Result login(EmployeeLoginDTO employeeLoginDTO);
}
