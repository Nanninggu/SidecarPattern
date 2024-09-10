package com.design_pattern.sidecar_pattern.mapper;

import com.design_pattern.sidecar_pattern.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();

    List<User> findByName(@Param("name") String name);
}
