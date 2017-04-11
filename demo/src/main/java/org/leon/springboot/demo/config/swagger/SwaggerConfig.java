package org.leon.springboot.demo.config.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by leon on 2016/9/16.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Autowired
    private Swagger swagger;

    @Bean
    public Docket swaggerSpringfoxDocket() {
        logger.debug("Starting Swagger");
        String packagePath = this.getClass().getPackage().getName();
        packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));
        packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));
        logger.debug("base package:" + packagePath);
        StopWatch watch = new StopWatch();
        watch.start();
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .host(swagger.getHost())
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(packagePath))
                .paths(PathSelectors.any())
                .build();
        watch.stop();
        logger.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(swagger.getApiInfo().getTitle())
                .description(swagger.getApiInfo().getDescription())
                .termsOfServiceUrl(swagger.getApiInfo().getServiceTerms())
                .contact(new Contact(swagger.getApiInfo().getContact().getName(),
                        swagger.getApiInfo().getContact().getUrl(),
                        swagger.getApiInfo().getContact().getEmail()))
                .license(swagger.getApiInfo().getLicense())
                .licenseUrl(swagger.getApiInfo().getLicenseUrl())
                .version(swagger.getApiInfo().getVersion())
                .build();
    }
}
