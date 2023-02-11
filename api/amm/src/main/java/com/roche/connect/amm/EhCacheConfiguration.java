package com.roche.connect.amm;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

@Configuration 
@EnableCaching
public class EhCacheConfiguration implements ApplicationContextAware {
    
    private ApplicationContext applicationContext; 
    
    @Bean 
    public EhCacheManagerFactoryBean ehCacheManagerFactory() throws IOException {
        final EhCacheManagerFactoryBean ehCacheManagerFactory = new EhCacheManagerFactoryBean();
        Resource resource = null;
        final PathMatchingResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver(applicationContext.getClassLoader());
        try {
            final Resource[] resources = resolver.getResources("classpath*:ehcache.xml");
            for (final Resource r: resources) {
                resource = r;
            }
            ehCacheManagerFactory.setCacheManagerName("ammEhCache");
            ehCacheManagerFactory.setConfigLocation(resource);
            ehCacheManagerFactory.setShared(true);
        } catch (IOException e) {
            logTheError("found error during ehcache initillization in EhCacheConfiguration::" + e.getMessage());
            throw e;
        }
        return ehCacheManagerFactory;
    }
    
    @Bean 
    public EhCacheCacheManager cacheManager() throws IOException {
        final EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        try {
            ehCacheCacheManager.setCacheManager(ehCacheManagerFactory().getObject());
        } catch (IOException e) {
            logTheError("found error during ehcache initillization in EhCacheConfiguration::" + e.getMessage());
            throw e;
        }
        return ehCacheCacheManager;
    }
    
    @Override 
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    
    private void logTheError(String errorMessage) {
        HMTPLoggerFactory.getLogger(this.getClass().getName()).error(errorMessage);
    }
}
