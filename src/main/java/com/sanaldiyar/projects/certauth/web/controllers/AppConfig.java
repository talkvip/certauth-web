/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.controllers;

import com.sanaldiyar.projects.certauth.web.security.SecurityManager;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author kazim
 */
@Configuration
@ComponentScan(basePackages = {"com.sanaldiyar.projects.certauth.web"})
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/ui/");
        resolver.setViewClass(UIView.class);
        return resolver;
    }

    @Bean
    public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter() {
        AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
        MappingJacksonHttpMessageConverter jacksonHttpMessageConverter=new MappingJacksonHttpMessageConverter();
        HttpMessageConverter[] converters=new HttpMessageConverter[]{jacksonHttpMessageConverter};
        adapter.setMessageConverters(converters);
        return adapter;
    } 
    
}
