package com.roche.connect.dpcr.engine.bl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * We need to exclude any controllers from being scanned so they are scanned at the appropriate time during startup so
 * the parent - child relationship is established correctly.  This is necessary in order to support multiple controllers
 * each connected to a different port.
 */
@Configuration
@ComponentScan(basePackages = "com.roche.connect.dpcr")
public class AppConfig {

/**
    @Bean
    LanguageModel languageModel() {
        return new LanguageModel();
    }

    @Bean
    LanguageController languageController() {
        return new LanguageController(languageModel());
    }

    @Bean
    MessageModel messageModel() {
        return new MessageModel();
    }*/
}
