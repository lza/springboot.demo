package org.leon.springboot.demo.twoFactorAuth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by leon on 2017/4/24.
 */
@Configuration
@ConfigurationProperties(prefix = "twoFactorAuth")
public class TwoFactorAuthCfg {
    private  boolean enable;
    private String path;
    private String organization;
    private List<String> excludes;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
