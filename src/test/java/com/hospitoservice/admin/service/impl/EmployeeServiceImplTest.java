package com.hospitoservice.admin.service.impl;

import com.hospitoservice.admin.exception.ResourceNotFoundException;
import com.hospitoservice.admin.model.*;
import com.hospitoservice.admin.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private Employee testEmployee;
    private final String testEmployeeId = "EMP001";

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(employeeRepository);
        
        // Setup test employee
        testEmployee = new Employee();
        testEmployee.setId("1");
        
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setFirstName("John");
        personalDetails.setLastName("Doe");
//        personalDetails.setEmail("john.doe@example.com");
        testEmployee.setPersonalDetails(personalDetails);
        
        EmploymentDetails employmentDetails = new EmploymentDetails();
        employmentDetails.setEmployeeId(testEmployeeId);
        employmentDetails.setDepartment("IT");
        employmentDetails.setDesignation("Software Engineer");
        testEmployee.setEmploymentDetails(employmentDetails);
        
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPhone("+1234567890");
        testEmployee.setContactDetails(contactDetails);
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of(testEmployee));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(testEmployeeId, employees.get(0).getEmploymentDetails().getEmployeeId());
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findById("1")).thenReturn(Optional.of(testEmployee));

        // Act
        Employee found = employeeService.getEmployeeById("1");

        // Assert
        assertNotNull(found);
        assertEquals(testEmployeeId, found.getEmploymentDetails().getEmployeeId());
    }

    @Test
    void getEmployeeById_WhenEmployeeNotExists_ShouldThrowException() {
        // Arrange
        when(employeeRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById("999"));
    }

    @Test
    void getEmployeeByEmployeeId_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findByEmploymentDetails_EmployeeId(testEmployeeId))
                .thenReturn(Optional.of(testEmployee));

        // Act
        Optional<Employee> result = employeeService.getEmployeeByEmployeeId(testEmployeeId);

        // Assert
        assertTrue(result.isPresent());
        Employee found = result.get();
        assertEquals(testEmployeeId, found.getEmploymentDetails().getEmployeeId());
    }

    @Test
    void createEmployee_WithValidData_ShouldReturnCreatedEmployee() {
        // Arrange
        when(employeeRepository.existsByEmploymentDetails_EmployeeId(testEmployeeId)).thenReturn(false);
        when(employeeRepository.existsByContactDetails_Email(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        // Act
        Employee created = employeeService.createEmployee(testEmployee);

        // Assert
        assertNotNull(created);
        assertEquals(testEmployeeId, created.getEmploymentDetails().getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_WithExistingEmployeeId_ShouldThrowException() {
        // Arrange
        when(employeeRepository.existsByEmploymentDetails_EmployeeId(testEmployeeId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(testEmployee));
    }

    @Test
    void updateEmployee_WithValidData_ShouldReturnUpdatedEmployee() {
        // Arrange
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId("1");
        
        PersonalDetails updatedDetails = new PersonalDetails();
        updatedDetails.setFirstName("Jane");
        updatedEmployee.setPersonalDetails(updatedDetails);
        
        when(employeeRepository.findById("1")).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        Employee result = employeeService.updateEmployee("1", updatedEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getPersonalDetails().getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(employeeRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> employeeService.updateEmployee("999", new Employee()));
    }

    @Test
    void deleteEmployee_WithExistingId_ShouldDeleteEmployee() {
        // Arrange
        when(employeeRepository.existsById("1")).thenReturn(true);
        doNothing().when(employeeRepository).deleteById("1");

        // Act
        employeeService.deleteEmployee("1");

        // Assert
        verify(employeeRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteEmployee_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(employeeRepository.existsById("999")).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> employeeService.deleteEmployee("999"));
    }

    @Test
    void existsByEmployeeId_WhenEmployeeExists_ShouldReturnTrue() {
        // Arrange
        when(employeeRepository.existsByEmploymentDetails_EmployeeId(testEmployeeId)).thenReturn(true);

        // Act
        boolean exists = employeeService.existsByEmployeeId(testEmployeeId);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Arrange
        String email = "test@example.com";
        when(employeeRepository.existsByContactDetails_Email(email)).thenReturn(true);

        // Act
        boolean exists = employeeService.existsByEmail(email);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByPhone_WhenPhoneExists_ShouldReturnTrue() {
        // Arrange
        String phone = "+1234567890";
        when(employeeRepository.existsByContactDetails_Phone(phone)).thenReturn(true);

        // Act
        boolean exists = employeeService.existsByPhone(phone);

        // Assert
        assertTrue(exists);
    }

    @Test
    void createEmployee_ShouldEncodePassword() {
        // Arrange
        String rawPassword = "password123";
        testEmployee.setPassword(rawPassword);
        
        when(employeeRepository.existsByEmploymentDetails_EmployeeId(testEmployeeId)).thenReturn(false);
        when(employeeRepository.existsByContactDetails_Email(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee savedEmployee = invocation.getArgument(0);
            savedEmployee.setId("1");
            return savedEmployee;
        });

        // Act
        Employee created = employeeService.createEmployee(testEmployee);

        // Assert
        assertNotNull(created);
        assertNotEquals(rawPassword, created.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, created.getPassword()));
    }

    @Test
    void updateEmployee_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        Employee existingEmployee = new Employee();
        existingEmployee.setId("1");
        
        PersonalDetails existingDetails = new PersonalDetails();
        existingDetails.setFirstName("John");
        existingDetails.setLastName("Doe");
        existingEmployee.setPersonalDetails(existingDetails);
        
        Employee updatedEmployee = new Employee();
        PersonalDetails updatedDetails = new PersonalDetails();
        updatedDetails.setFirstName("Jane");
        updatedEmployee.setPersonalDetails(updatedDetails);
        
        when(employeeRepository.findById("1")).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Employee result = employeeService.updateEmployee("1", updatedEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getPersonalDetails().getFirstName());
        assertEquals("Doe", result.getPersonalDetails().getLastName()); // Should remain unchanged
        assertNotNull(result.getUpdatedAt());
    }
}
