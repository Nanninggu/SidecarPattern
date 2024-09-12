package com.design_pattern.sidecar_pattern.mapper;

import com.design_pattern.sidecar_pattern.dto.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    void insertDepartment(Department department);

    List<Department> getDepartments();

    List<Department> getDepartmentById(Department department_id);
}
