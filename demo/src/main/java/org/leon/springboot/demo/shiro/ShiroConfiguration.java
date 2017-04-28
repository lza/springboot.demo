package org.leon.springboot.demo.shiro;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.leon.springboot.demo.config.FilterOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * TODO add description
 */
@Configuration
public class ShiroConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShiroCfg shiroCfg;
    @Autowired
    private AuthorizingRealm realm;
    @Autowired
    private AbstractSessionDAO sessionDao;

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setOrder(FilterOrder.ShiroFilterOrder);
        filterRegistrationBean.addUrlPatterns(shiroCfg.getPath());
        return filterRegistrationBean;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        logger.debug("shiro twoFactorAuth factory bean");
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager());
        return factoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        final DefaultWebSecurityManager securityManager
                = new DefaultWebSecurityManager();
        realm.setCredentialsMatcher(credentialsMatcher());
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        final DefaultWebSessionManager sessionManager
                = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDao);
        sessionManager.setGlobalSessionTimeout(shiroCfg.getSessionTimeout());
        return sessionManager;
    }

    @Bean(name = "credentialsMatcher")
    public PasswordMatcher credentialsMatcher() {
        final PasswordMatcher credentialsMatcher = new PasswordMatcher();
        credentialsMatcher.setPasswordService(passwordService());
        return credentialsMatcher;
    }

    @Bean(name = "passwordService")
    public DefaultPasswordService passwordService() {
        return new DefaultPasswordService();
    }
}
