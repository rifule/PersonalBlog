package com.blog.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(new Info()
                .title("个人博客 API")
                .description("个人博客系统后端 API 接口文档")
                .version("1.0")
                .contact(new Contact()
                        .name("Blog Team")
                        .email("blog@example.com")
                        .url("https://example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));

        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080").description("开发环境"));
        servers.add(new Server().url("https://api.example.com").description("生产环境"));
        openAPI.setServers(servers);

        return openAPI;
    }
}
