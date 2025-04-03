package com.c.study.config.knife4j;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 文档配置
 *
 * @author zhang
 */
@Configuration
@EnableKnife4j
public class SwaggerConfiguration {

    @Value("${server.port:8000}")
    private String port;

    private ApiKey apiKey() {
        return new ApiKey("saToken", "sa-token", "header");
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("saToken", authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).operationSelector(new Predicate<OperationContext>() {
            @Override
            public boolean test(OperationContext operationContext) {
                return true;
            }
        }).build();
    }

    @Bean
    public Docket authRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("图书管理-v1.0.0")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.c.study.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext())).securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiInfo apiInfo() {
        Contact c = new Contact("c", "", "");
        String url = String.format("http://localhost:%s/", port);
        return new ApiInfoBuilder()
                .title("图书管理系统")
                .description("图书管理")
                .termsOfServiceUrl(url)
                .contact(c)
                .version("1.0")
                .build();
    }
}
