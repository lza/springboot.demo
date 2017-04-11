package org.leon.springboot.demo;

import org.leon.springboot.demo.config.cors.UrlBasedCorsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class App extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
            }
        };
    }

    @Autowired
    private UrlBasedCorsConfiguration urlBasedCorsConfiguration;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                Map<String, CorsConfiguration> configs = urlBasedCorsConfiguration.getConfigs();
                CorsConfiguration corsConfiguration;
                CorsRegistration corsRegistration;
                List<String> list;
                Boolean yes;
                Long age;
                for (String path: configs.keySet()){
                    corsConfiguration = configs.get(path);
                    corsRegistration = registry.addMapping(path);
                    list = corsConfiguration.getAllowedOrigins();
                    if(list != null){
                        corsRegistration.allowedOrigins(list.toArray(new String[0]));
                    }
                    list = corsConfiguration.getAllowedMethods();
                    if(list != null){
                        corsRegistration.allowedMethods(list.toArray(new String[0]));
                    }
                    list = corsConfiguration.getAllowedHeaders();
                    if(list != null){
                        corsRegistration.allowedHeaders(list.toArray(new String[0]));
                    }
                    list = corsConfiguration.getExposedHeaders();
                    if(list != null){
                        corsRegistration.exposedHeaders(list.toArray(new String[0]));
                    }
                    yes = corsConfiguration.getAllowCredentials();
                    if(yes != null){
                        corsRegistration.allowCredentials(yes);
                    }
                    age = corsConfiguration.getMaxAge();
                    if(age != null){
                        corsRegistration.maxAge(age);
                    }
                }
            }
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}