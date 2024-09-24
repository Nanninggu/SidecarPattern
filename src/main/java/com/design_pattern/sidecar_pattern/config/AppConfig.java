package com.design_pattern.sidecar_pattern.config;

import com.design_pattern.sidecar_pattern.util.UserRoleChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRoleChecker userRoleChecker() {
        return new UserRoleChecker();
    }
}
