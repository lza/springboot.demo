package org.leon.springboot.demo.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leon on 2017/4/24.
 */
@Configuration
@ConfigurationProperties(prefix = "shiroAuth")
public class ShiroCfg {
    private String path;
    private long sessionTimeout;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
