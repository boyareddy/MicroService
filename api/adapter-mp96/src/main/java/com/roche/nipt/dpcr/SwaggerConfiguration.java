package com.roche.nipt.dpcr;

import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableSwagger2
public class SwaggerConfiguration {
   /** @Bean
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
    }*/
}