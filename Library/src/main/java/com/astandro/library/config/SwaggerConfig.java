package com.astandro.library.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Library API", version = "1.0", description = "API documentation for Library Management System"))
public class SwaggerConfig {
}
