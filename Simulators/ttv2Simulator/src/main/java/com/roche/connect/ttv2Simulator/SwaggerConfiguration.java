package com.roche.connect.ttv2Simulator;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket productApi(final @Value("${pas.version}") String version,
        final @Value("${pas.title}") String title, final @Value("${pas.api_description}") String description,
        final @Value("${pas.contact_email}") String email) {
        
        ApiInfo apiInfo = new ApiInfoBuilder().title(title)
            .description(description)
            .contact(email)
            .version(version).build();
        
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(or(regex("/*.*")))
                .build()
                .apiInfo(apiInfo);
    }
    
    @Component
    @Primary
    public static class CombinedSwaggerResources implements SwaggerResourcesProvider {

        @Resource
        private InMemorySwaggerResourcesProvider inMemorySwaggerResourcesProvider;

        @Override
        public List<SwaggerResource> get() {

            SwaggerResource jaxRsResource = new SwaggerResource();
            jaxRsResource.setLocation("/json/swagger.json");
            jaxRsResource.setSwaggerVersion("2.0");
            jaxRsResource.setName("PAS-Documentation");

            return Stream.concat(Stream.of(jaxRsResource), inMemorySwaggerResourcesProvider.get().stream()).collect(Collectors.toList());
        }

    }
}

/**
 * ============================================================================
 * ============ COPYRIGHT NOTICE
 * ================================================
 * ======================================== Copyright (C) 2018, HCL Technologies
 * Limited. All Rights Reserved. Proprietary and confidential. All information
 * contained herein is, and remains the property of HCL Technologies Limited.
 * Copying or reproducing the contents of this file, via any medium is strictly
 * prohibited unless prior written permission is obtained from HCL Technologies
 * Limited.
 */