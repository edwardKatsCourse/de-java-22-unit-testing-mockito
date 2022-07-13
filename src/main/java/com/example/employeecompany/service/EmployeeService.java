package com.example.employeecompany.service;

import com.example.employeecompany.dto.EmployeeRequestDto;
import com.example.employeecompany.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {

    void createEmployee(EmployeeRequestDto request);
    void deleteEmployee(Long id);

    EmployeeResponseDto findById(Long id);

    List<EmployeeResponseDto> findEmployeesByCompany(Long companyId);

}
