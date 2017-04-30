package org.leon.springboot.demo.twoFactorAuth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.leon.springboot.demo.Utils;
import org.leon.springboot.demo.db.dao.UserMapper;
import org.leon.springboot.demo.db.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * Created by leon on 2017/4/22.
 */
@Component
public class TwoFactorAuthInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwoFactorAuthCfg twoFactorAuthCfg;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if(!twoFactorAuthCfg.isEnable()){
            logger.info("two factor auth disabled");
            return true;
        }
        String servletPath = request.getServletPath();
        logger.debug("servlet path:{}", servletPath);
        String url = servletPath;
        for (String exclude:twoFactorAuthCfg.getExcludes()){
            if(url.startsWith(exclude)){
                logger.debug("{} exclude from tow factor auth", exclude);
                return true;
            }
        }

        logger.debug("tow factor interceptor:{}", handler.getClass());
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        for (Annotation annotation : handlerMethod.getMethod().getDeclaredAnnotations()){
            logger.debug("annotation name:{}", annotation.annotationType().getName());
            logger.debug("annotation string:{}", annotation.toString());
        }

        if(!handlerMethod.hasMethodAnnotation(RequiresAuthentication.class)){
            logger.debug("not RequiresAuthentication");
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            logger.debug("login");

            boolean permitted = false;
            if(handlerMethod.hasMethodAnnotation(RequiresRoles.class)){
                RequiresRoles requiresRoles = handlerMethod.getMethodAnnotation(RequiresRoles.class);
                String[] roles = requiresRoles.value();
                if(requiresRoles.logical() == Logical.AND){
                    try {
                        subject.checkRoles(roles);
                        permitted = true;
                    } catch (AuthorizationException e){}

                }else {
                    for (String role:roles){
                        try {
                            subject.checkRoles(role);
                            permitted = true;
                            break;
                        }catch (AuthorizationException e){}
                    }
                }
            }
            if(handlerMethod.hasMethodAnnotation(RequiresPermissions.class)){
                RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
                String[] permissions = requiresPermissions.value();
                if(requiresPermissions.logical() == Logical.AND){
                    try {
                        subject.checkPermissions(permissions);
                        permitted = true;
                    }catch (AuthorizationException e){}
                }else {
                    for (String permission:permissions){
                        try {
                            subject.checkPermissions(permission);
                            permitted = true;
                        }catch (AuthorizationException e){}
                    }
                }
            }
            if(!permitted){
                logger.debug("have no permission");
                return true;
            }

            Session session = subject.getSession();
            String email = (String) subject.getPrincipal();
            User user = userMapper.findByEmailAndActive(email, true);
            if (user != null) {
                boolean otpEnabled = user.getTwoFactorAuthEnabled();
                boolean otpActivated = user.getTwoFactorAuthActivated();
                boolean isOTPverified = (boolean) session.getAttribute("isOTPverified");
                logger.debug("otpEnabled:{}", otpEnabled);
                logger.debug("otpActivated:{}", otpActivated);
                logger.debug("isOTPverified:{}", isOTPverified);

                if(otpEnabled){
                    if(!otpActivated){
                        Utils.generateAuthFailResp(response, "401.4", "two factor auth reset, active required");
                        return false;
                    }
                    if(!isOTPverified){
                        Utils.generateAuthFailResp(response, "401.5", "two factor auth required");
                        return false;
                    }
                }
            } else {
                logger.error("user not found");
            }
        }
        return true;
    }
}
