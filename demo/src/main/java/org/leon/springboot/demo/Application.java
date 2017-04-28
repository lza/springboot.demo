package org.leon.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    private static ApplicationContext applicationContext = null;
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void main(String... args) {
        applicationContext = SpringApplication.run(Application.class, args);
    }
}