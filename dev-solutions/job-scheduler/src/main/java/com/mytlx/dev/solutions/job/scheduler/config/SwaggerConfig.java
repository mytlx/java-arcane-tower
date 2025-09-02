package com.mytlx.dev.solutions.job.scheduler.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 17:47:13
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI jobSchedulerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("任务调度系统接口文档")
                        .description("包含任务管理、日志查看等接口")
                        .version("v1.0.0")
                        .contact(new Contact().name("TLX").email("mytlx1466@gmail.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("项目地址")
                        .url("https://github.com/your_repo"));
    }

    @Bean
    public GroupedOpenApi jobApiGroup() {
        return GroupedOpenApi.builder()
                .group("job-scheduler")
                .packagesToScan("com.mytlx.dev.solutions.job.scheduler.controller")
                .build();
    }

}
