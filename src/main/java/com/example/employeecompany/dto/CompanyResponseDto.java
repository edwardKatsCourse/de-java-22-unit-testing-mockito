package com.example.employeecompany.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyResponseDto {

    private Long id;
    private String name;

    private List<EmployeeResponseDto> employees;
}
