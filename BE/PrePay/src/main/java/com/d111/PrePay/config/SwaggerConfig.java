package com.d111.PrePay.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API 문서")
                        .description("Swagger에서 로그인 API 수동 등록")
                        .version("1.0"))
                .paths(new Paths().addPathItem("/user/login", new PathItem()
                        .post(new io.swagger.v3.oas.models.Operation()
                                .summary("로그인 API")
                                .description("Spring Security에서 처리하는 로그인 API")
                                .addTagsItem("Authentication")
                                .responses(new ApiResponses().addApiResponse("200", new ApiResponse().description("로그인 성공"))
                                        .addApiResponse("401", new ApiResponse().description("인증 실패"))))));
    }
}
