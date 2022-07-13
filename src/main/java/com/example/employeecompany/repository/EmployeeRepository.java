package com.example.employeecompany.repository;

import com.example.employeecompany.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByCompany_Id(Long companyId);
}
