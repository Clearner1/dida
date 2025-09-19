package com.yzr.dida.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "didaApi")
    public Docket didaApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("滴答清单开放API接口文档")
                        .description("滴答清单开放API集成接口文档，提供任务和项目管理的RESTful API接口")
                        .termsOfServiceUrl("https://developer.dida365.com/")
                        .contact("yzremail@163.com")
                        .version("1.0")
                        .build())
                .groupName("滴答清单API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yzr.dida.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "authApi")
    public Docket authApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("认证授权接口")
                        .description("OAuth2认证授权相关接口")
                        .version("1.0")
                        .build())
                .groupName("认证授权")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yzr.dida.controller"))
                .paths(PathSelectors.regex("/auth.*"))
                .build();
    }

    @Bean(value = "projectApi")
    public Docket projectApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("项目管理接口")
                        .description("项目相关的CRUD操作接口")
                        .version("1.0")
                        .build())
                .groupName("项目管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yzr.dida.controller"))
                .paths(PathSelectors.regex("/open/v1/project.*"))
                .build();
    }

    @Bean(value = "taskApi")
    public Docket taskApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("任务管理接口")
                        .description("任务相关的CRUD操作接口")
                        .version("1.0")
                        .build())
                .groupName("任务管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yzr.dida.controller"))
                .paths(PathSelectors.regex("/open/v1/project/.*/task.*"))
                .build();
    }
}