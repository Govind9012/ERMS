package com.example.Admin.repository;

import com.example.Admin.entity.enums.Role;
import com.example.Admin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmployeeId(String employeeId);

    List<Employee> findByRoleIn(List<Role> roles);

    boolean existsByEmployeeId(String employeeId);

    Optional<Employee> findByEmployeeIdIgnoreCase(String employeeId);

    Optional<Employee> findByNameIgnoreCase(String name);

    boolean existsByPocId(UUID pocId);

    List<Employee> findByPocId(UUID pocId);
}
