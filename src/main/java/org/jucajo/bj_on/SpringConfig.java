package org.jucajo.bj_on;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info().title("Spring Doc").version("1.0").description("Spring Doc")
        );
    }
}
