package org.leon.springboot.demo.config.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leon on 2016/10/11.
 */
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class Swagger {
    private String host;
    private ApiInfo apiInfo;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }
}
