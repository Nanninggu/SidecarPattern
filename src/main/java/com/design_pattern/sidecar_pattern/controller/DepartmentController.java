package com.design_pattern.sidecar_pattern.controller;

import com.design_pattern.sidecar_pattern.Handler.ErrorHandler;
import com.design_pattern.sidecar_pattern.dto.Department;
import com.design_pattern.sidecar_pattern.mapper.DepartmentMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @GetMapping("/department")
    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackGetDepartment")
    public List<Department> getDepartment() {
        return departmentMapper.getDepartments();
    }

    @GetMapping("/department/error")
    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackErrorDepartment")
    public List<Department> errorDepartment(Department department_id) {
        try {
            List<Department> departments = departmentMapper.getDepartmentById(department_id);
            for (Department department : departments) {
                if ("ERROR".equals(department.getDepartment_id())) {
                    throw new RuntimeException("Department ID cannot be 'ERROR'");
                }
            }
            return departments;
        } catch (Exception ex) {
            ErrorHandler.logErrorAndThrow(this.getClass().getName(), "errorDepartment", ex);
            return null;
        }
    }

    @PostMapping("/department/insert")
    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackInsertDepartment")
    public void insertDepartment() {
        Department department = new Department();
        department.setDepartment_id("1");
        department.setDepartment_name("HR");
        departmentMapper.insertDepartment(department);
    }

    // Fallback methods
    public List<Department> fallbackGetDepartment(Throwable t) {
        globalExceptionHandler.handleAllExceptions(new RuntimeException(t), null);
        return List.of();
    }

    // 에러가 발생 했을때 대체 사이트로 이동
    public List<Department> fallbackErrorDepartment(Department department_id, Throwable t) {
        globalExceptionHandler.handleAllExceptions(new RuntimeException(t), null);
        RestTemplate restTemplate = new RestTemplate();
        String userServiceUrl = "http://localhost:8080/common";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(userServiceUrl, String.class);
            System.out.println("Fallback API response: " + response.getBody());
            return List.of();
        } catch (RestClientException e) {
            System.err.println("Error while calling fallback API: " + e.getMessage());
            return List.of();
        }
    }

    public void fallbackInsertDepartment(Throwable t) {
        globalExceptionHandler.handleAllExceptions(new RuntimeException(t), null);
    }
}
