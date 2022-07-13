package com.example.employeecompany.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeResponseDto {

    private Long id;
    private String name;

    private Long companyId;
    private String companyName;
}
