package no.ntnu.idata2306.group6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {

    @Bean
    public Docket webApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("no.ntnu.idata2306.group6.controller.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket apiApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("no.ntnu.idata2306.group6.controller.api"))
                .paths(PathSelectors.any())
                .build();
    }
}
