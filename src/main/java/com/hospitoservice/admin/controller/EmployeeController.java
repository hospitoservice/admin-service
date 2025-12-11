package com.hospitoservice.admin.controller;

import com.hospitoservice.admin.exception.ResourceNotFoundException;
import com.hospitoservice.admin.model.Employee;
import com.hospitoservice.admin.service.EmployeeService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @QueryMapping
    public List<Employee> allEmployees() {
        return employeeService.getAllEmployees();
    }

    @QueryMapping
    public Employee employeeById(@Argument String id) {
        return employeeService.getEmployeeById(id);
    }

    @MutationMapping
    public Employee createEmployee(@Argument("employee") Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @MutationMapping
    public Employee updateEmployee(
            @Argument String id,
            @Argument("employee") Employee employee
    ) {
        return employeeService.updateEmployee(id, employee);
    }

    @MutationMapping
    public Boolean deleteEmployee(@Argument String id) {
        employeeService.deleteEmployee(id);
        return true;
    }

    // Additional REST endpoints for non-GraphQL clients
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<Employee> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        return employeeService.getEmployeeByEmployeeId(employeeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", employeeId));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployeeRest(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeRest(
            @PathVariable String id,
            @RequestBody Employee employee
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeRest(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
