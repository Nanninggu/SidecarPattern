package com.design_pattern.sidecar_pattern.controller;

import com.design_pattern.sidecar_pattern.dto.User;
import com.design_pattern.sidecar_pattern.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userMapper.findAll();
    }

    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String name) {
        String projectInfo = "Project: Sidecar Pattern, Version: 1.0.0";
        if ("user10".equals(name)) {
            throw new RuntimeException("Intentional error: User with name 'user10' found." + projectInfo);
        }
        return userMapper.findByName(name);
    }

    @GetMapping("/log")
    public String log() {
        String sidecarServiceUrl = "http://localhost:8081/log";
        return restTemplate.getForObject(sidecarServiceUrl, String.class);
    }

    @GetMapping("/new-api-error")
    public String newApiError() {
        String projectInfo = "Project: Sidecar Pattern, Version: 1.0.0";
        String className = this.getClass().getSimpleName();
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String errorDetails = "Error Type: Intentional Error";
        throw new RuntimeException("Intentional error for testing. " + projectInfo + ", Class: " + className + ", Method: " + methodName + ". " + errorDetails);
    }

    @GetMapping("/fetch-log-json")
    public Object fetchLogJson() {
        String sidecarServiceUrl = "http://localhost:8081/log";
        return restTemplate.getForObject(sidecarServiceUrl, Object.class);
    }
}
