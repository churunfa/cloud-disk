package com.cloud.securityservice.config;

import org.churunfa.security.password.JWT.JWTUtils;
import org.churunfa.security.password.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

    @Bean
    BCryptPasswordEncoder BCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTUtils JwtUtils() {
        return new JWTUtils();
    }

}
