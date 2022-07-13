package com.example.employeecompany.service.impl;

import com.example.employeecompany.dto.EmployeeRequestDto;
import com.example.employeecompany.dto.EmployeeResponseDto;
import com.example.employeecompany.entity.Company;
import com.example.employeecompany.entity.Employee;
import com.example.employeecompany.repository.CompanyRepository;
import com.example.employeecompany.repository.EmployeeRepository;
import com.example.employeecompany.repository.MyRepository;
import com.example.employeecompany.repository.impl.MyRepositoryImpl;
import com.example.employeecompany.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock // Mockito.mock(CompanyRepository)
    private CompanyRepository companyRepository;

    @InjectMocks // where to put all mocks
    private EmployeeServiceImpl service; // new EmployeeServiceImpl(employeeRepository, companyRepository);


    @Test
    public void abc() {
//        EmployeeServiceImpl service = new EmployeeServiceImpl()

        MyRepository repository = Mockito.spy(MyRepositoryImpl.class);
//        MyRepository repository = new MockitoProxyRepository();

        Mockito
                .when(repository.findById(1L))
                .thenReturn("one");


        // mock
        System.out.println(repository.findById(1L));

        // spy
        System.out.println(repository.findById(2L));
        ;

        // Mockito
        // - Spy
        // - Mock
    }

    @Test
    @DisplayName("should throw 404-NOT_FOUND, when no such company")
    public void shouldThrow404WhenNoSuchCompany() {

        // интерфейс = новый Мокито_мне_все_равно_какой_класс_ты_там_создашь();
//        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//        CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);

//        EmployeeService service = new EmployeeServiceImpl(
//                employeeRepository,
//                companyRepository
//        );

        EmployeeRequestDto request = EmployeeRequestDto.builder()
                .companyId(100L)
                .name("John Doe")
                .build();

        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        String expectedMessage = String.format("Company with id [%s] does not exist", request.getCompanyId());

        Mockito
                .when(companyRepository.findById(100L))
                .thenReturn(Optional.empty());


        ResponseStatusException ex = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> service.createEmployee(request)
        );

        Assertions.assertEquals(expectedStatus, ex.getStatus());
        Assertions.assertEquals(expectedMessage, ex.getReason());
    }

    @Test
    @DisplayName("should save() employee, when there is such company")
    public void shouldSaveEmployeeWhenThereIsSuchCompanyTest() {
        EmployeeRequestDto request = EmployeeRequestDto.builder()
                .name("John Doe")
                .companyId(1000L)
                .build();

        Company company = Company.builder()
                .companyName("Abc Company")
                .id(request.getCompanyId())
                .build();

        Employee employee = Employee.builder()
                .company(company)
                .name(request.getName())
                .id(1L)
                .build();

        Mockito
                .when(companyRepository.findById(request.getCompanyId()))
                .thenReturn(Optional.of(company));

        Mockito
                .when(employeeRepository.save(
                        ArgumentMatchers.argThat(
                                savedEmployee -> {
                                    boolean isSameName = savedEmployee.getName().equals(request.getName());
                                    boolean isSameCompanyName = savedEmployee.getCompany().getCompanyName().equals(company.getCompanyName());
                                    boolean isSameCompanyId = savedEmployee.getCompany().getId().equals(company.getId());

                                    return isSameName && isSameCompanyName && isSameCompanyId;
                                })))
                .thenReturn(employee);

        service.createEmployee(request);
    }

    @Test
    public void shouldCallDeleteEmployeeById() {
        Long requestId = 1L;

        // Stub
        // Mockito.mock
        // Mockito.spy
        // Mockito.doNothing() -> void

        Mockito.doNothing()// void methods
                .when(employeeRepository)
                .deleteById(requestId);

        service.deleteEmployee(requestId);

        Mockito
                .verify(
                        employeeRepository,
                        Mockito.times(1))
                .deleteById(requestId);
    }

    @Test
    public void shouldThrow404WhenNoSuchEmployee() {
        Long employeeId = 1L;
        HttpStatus expectedErrorStatus = HttpStatus.NOT_FOUND;
        String expectedErrorMessage = String.format("Employee with such id [%s] does not exist", employeeId);

        Mockito
                .when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());


        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> service.findById(employeeId)
        );

        Assertions.assertEquals(expectedErrorStatus, exception.getStatus());
        Assertions.assertEquals(expectedErrorMessage, exception.getReason());

    }

    @Test
    public void shouldReturnDtoWhenThereIsSuchEmployeeAndCompanyIsNotNull() {
        Long requestedId = 1L;
        Employee employee = Employee.builder()
                .name("John Doe")
                .id(requestedId)
                .company(Company.builder()
                        .id(1000L)
                        .companyName("Abc Company")
                        .build())
                .build();

        Mockito
                .when(employeeRepository.findById(requestedId))
                .thenReturn(Optional.of(employee));

        EmployeeResponseDto response = service.findById(requestedId);

        Assertions.assertEquals(employee.getId(), response.getId());
        Assertions.assertEquals(employee.getName(), response.getName());

        Assertions.assertNotNull(response.getCompanyId());
        Assertions.assertNotNull(response.getCompanyName());

        Assertions.assertEquals(employee.getCompany().getCompanyName(), response.getCompanyName());
        Assertions.assertEquals(employee.getCompany().getId(), response.getCompanyId());
    }

    @Test
    public void shouldReturnDtoWhenThereIsSuchEmployeeAndCompanyIsNull() {
        Long requestedId = 1L;
        Employee employee = Employee.builder()
                .name("John Doe")
                .id(requestedId)
                .company(null)
                .build();

        Mockito
                .when(employeeRepository.findById(requestedId))
                .thenReturn(Optional.of(employee));

        EmployeeResponseDto response = service.findById(requestedId);

        Assertions.assertEquals(employee.getId(), response.getId());
        Assertions.assertEquals(employee.getName(), response.getName());

        Assertions.assertNull(response.getCompanyId());
        Assertions.assertNull(response.getCompanyName());
    }

    @Test
    public void shouldReturnEmptyListWhenThereAreNoEmployees() {
        Long companyId = 1L;

        int expectedResponseSize = 0;

        Mockito
                .when(employeeRepository.findAllByCompany_Id(companyId))
                .thenReturn(List.of());

        var response = service.findEmployeesByCompany(companyId);

//        Assertions.assertEquals(expectedResponseSize, response.size());
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    public void shouldReturnEmployeesWhenThereAre() {
        Long companyId = 1L;

        List<Employee> employees = List.of(
                Employee.builder().id(100L).name("Anna").build(),
                Employee.builder().id(101L).name("Peter").build()
        );

        Mockito
                .when(employeeRepository.findAllByCompany_Id(companyId))
                .thenReturn(employees);

        var response = service.findEmployeesByCompany(companyId);

        Assertions.assertFalse(response.isEmpty());
    }

}

//class MockitoProxyRepository extends MyRepositoryImpl {
//    @Override
//    public String findById(Long id) {
//        return super.findById(id);
////        return "1-1";
//    }
//}
