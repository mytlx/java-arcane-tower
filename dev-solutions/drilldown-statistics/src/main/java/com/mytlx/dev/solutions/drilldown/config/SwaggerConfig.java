package com.mytlx.dev.solutions.drilldown.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-13 13:57
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("接口文档标题")
                        .version("1.0")
                        .description("接口文档描述")
                );
    }
}