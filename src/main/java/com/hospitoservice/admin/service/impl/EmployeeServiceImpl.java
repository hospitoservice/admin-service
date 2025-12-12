package com.hospitoservice.admin.service.impl;

import com.hospitoservice.admin.exception.ResourceNotFoundException;
import com.hospitoservice.admin.model.Employee;
import com.hospitoservice.admin.repository.EmployeeRepository;
import com.hospitoservice.admin.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmploymentDetails_EmployeeId(employeeId);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if (existsByEmployeeId(employee.getEmploymentDetails().getEmployeeId())) {
            throw new IllegalArgumentException("Employee with ID " + employee.getEmploymentDetails().getEmployeeId() + " already exists");
        }
        if (existsByEmail(employee.getContactDetails().getEmail())) {
            throw new IllegalArgumentException("Email " + employee.getContactDetails().getEmail() + " is already in use");
        }
        
        // Encode password if provided
//        if (employee.getPassword() != null) {
//            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//        }
        
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(String id, Employee employee) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    // Update fields that are allowed to be modified
                    if (employee.getPersonalDetails() != null) {
                        existingEmployee.setPersonalDetails(employee.getPersonalDetails());
                    }
                    if (employee.getContactDetails() != null) {
                        existingEmployee.setContactDetails(employee.getContactDetails());
                    }
                    if (employee.getEmploymentDetails() != null) {
                        existingEmployee.setEmploymentDetails(employee.getEmploymentDetails());
                    }
                    if (employee.getAddresses() != null) {
                        existingEmployee.setAddresses(employee.getAddresses());
                    }
                    if (employee.getDocumentDetails() != null) {
                        existingEmployee.setDocumentDetails(employee.getDocumentDetails());
                    }
                    if (employee.getPayrollDetails() != null) {
                        existingEmployee.setPayrollDetails(employee.getPayrollDetails());
                    }
                    if (employee.getRoles() != null) {
                        existingEmployee.setRoles(employee.getRoles());
                    }
                    if (employee.getPermissions() != null) {
                        existingEmployee.setPermissions(employee.getPermissions());
                    }
                    if (employee.getRemarks() != null) {
                        existingEmployee.setRemarks(employee.getRemarks());
                    }
                    
                    existingEmployee.setUpdatedAt(LocalDateTime.now());
                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
        return employeeRepository.existsByEmploymentDetails_EmployeeId(employeeId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByContactDetails_Email(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return employeeRepository.existsByContactDetails_Phone(phone);
    }
}
