package kr.co.suitcarrier.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("SuitCarrier Backend API").version(appVersion)
                .description("SuitCarrier 프로젝트의 backend API 문서입니다.")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));

        List<Server> servers = Arrays.asList(new Server().description("demo"));

        // http 기본 인증
        SecurityScheme basicAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("basic");
        SecurityRequirement securityItem = new SecurityRequirement().addList("basicAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth", basicAuth))
                .addSecurityItem(securityItem)
                .info(info);
    }
}
