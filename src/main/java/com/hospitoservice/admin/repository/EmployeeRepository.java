package com.hospitoservice.admin.repository;

import com.hospitoservice.admin.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByEmploymentDetails_EmployeeId(String employeeId);
    boolean existsByContactDetails_Email(String email);
    boolean existsByContactDetails_Phone(String phone);
    Optional<Employee> findByEmploymentDetails_EmployeeId(String employeeId);
}
