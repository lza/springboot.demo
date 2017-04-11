package org.leon.springboot.demo.config.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Map;

/**
 * Created by leon on 2016/11/7.
 */
@Configuration
@ConfigurationProperties(prefix = "cors")
public class UrlBasedCorsConfiguration {
    private Map<String, CorsConfiguration> configs;

    public Map<String, CorsConfiguration> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, CorsConfiguration> configs) {
        this.configs = configs;
    }
}
