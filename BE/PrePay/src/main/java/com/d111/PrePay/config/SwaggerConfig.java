package com.d111.PrePay.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.tags.Tag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API 문서")
                        .description("Swagger에서 로그인 API 수동 등록")
                        .version("1.0"))
                .paths(new Paths().addPathItem("/user/login", new PathItem()
                        .post(new Operation()
                                .summary("로그인 API")
                                .description("이메일과 비밀번호를 입력받아 JWT 토큰을 반환합니다.")
                                .addTagsItem("Authentication")
                                .requestBody(new RequestBody()
                                        .description("로그인 요청 정보")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<Map<String, String>>()
                                                        .addProperties("email", new Schema<String>().type("string").example("user@example.com"))
                                                        .addProperties("password", new Schema<String>().type("string").example("password123"))
                                                )
                                        ))
                                        .required(true)
                                )
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                                        .addApiResponse("401", new ApiResponse().description("인증 실패"))))));
    }
}
