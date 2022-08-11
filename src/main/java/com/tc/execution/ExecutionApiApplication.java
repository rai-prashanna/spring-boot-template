package com.tc.execution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class ExecutionApiApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ExecutionApiApplication.class, args);
    }
}
