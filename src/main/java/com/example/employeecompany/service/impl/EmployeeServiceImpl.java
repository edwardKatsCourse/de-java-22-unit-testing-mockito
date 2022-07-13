package com.example.employeecompany.service.impl;

import com.example.employeecompany.dto.EmployeeRequestDto;
import com.example.employeecompany.dto.EmployeeResponseDto;
import com.example.employeecompany.entity.Company;
import com.example.employeecompany.entity.Employee;
import com.example.employeecompany.repository.CompanyRepository;
import com.example.employeecompany.repository.EmployeeRepository;
import com.example.employeecompany.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    // service -> service2(service11, service15, service127) -> service3 -> repository

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    // same as

//    @Autowired
//    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
//        this.employeeRepository = employeeRepository;
//        this.companyRepository = companyRepository;
//    }

    @Override
    public void createEmployee(EmployeeRequestDto request) {

        Company company = companyRepository
                .findById(request.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Company with id [%s] does not exist", request.getCompanyId())
                ));

        Employee employee = Employee.builder()
                .company(company)
                .name(request.getName())
                .build();

        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDto findById(Long id) {
        Employee employee = employeeRepository
                .findById(id) // Optional.empty()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Employee with such id [%s] does not exist", id)
                ));


        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .companyId(employee.getCompany() == null ? null : employee.getCompany().getId())
                .companyName(employee.getCompany() == null ? null : employee.getCompany().getCompanyName())
                .build();
    }

    @Override
    public List<EmployeeResponseDto> findEmployeesByCompany(Long companyId) {

        return employeeRepository.findAllByCompany_Id(companyId).stream()
                .map(employee -> EmployeeResponseDto.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .companyId(employee.getCompany() == null ? null : employee.getCompany().getId())
                        .companyName(employee.getCompany() == null ? null : employee.getCompany().getCompanyName())
                        .build()
                )
                .toList();
    }
}
