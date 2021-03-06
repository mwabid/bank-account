package com.sg.bank.account.springconf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.sg.bank.account.rest"))
                //.paths(regex("/product.*"))
                    .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Bank Account API")
            .description("Bank Account API")
            .contact("mwabid@gmail.com")
            .license("MIT License")
            .version("0.0.1-SNAPSHOT")
            .build();
    }
}