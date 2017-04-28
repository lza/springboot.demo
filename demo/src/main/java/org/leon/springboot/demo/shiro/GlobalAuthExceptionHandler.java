package org.leon.springboot.demo.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.leon.springboot.demo.controller.rest.params.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by leon on 2017/4/25.
 */
@RestControllerAdvice
public class GlobalAuthExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShiroCfg shiroCfg;

    /*HTTP 401.1 – 未授权：登录失败
    HTTP 401.2 – 未授权：服务器配置问题导致登录失败
    HTTP 401.3 – ACL 禁止访问资源
    HTTP 401.4 – 未授权：授权被筛选器拒绝
    HTTP 401.5 – 未授权：ISAPI 或 CGI 授权失败*/

    /**
     * not login
     */
    @ExceptionHandler({UnauthenticatedException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<String> UnauthenticatedException() {
        logger.debug("UnauthenticatedException");
        BaseResult<String> result = new BaseResult<String>();
        result.setStatus("401.1");
        result.setMsg("login required");
        return result;
    }

    /***
     * login
     * user not exist
     */
    @ExceptionHandler({UnknownAccountException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<String> UnknownAccountException() {
        logger.debug("UnknownAccountException");
        BaseResult<String> result = new BaseResult<String>();
        result.setStatus("401.1");
        result.setMsg("user not exist");
        return result;
    }

    /***
     * login
     * user exist but password not correct
     */
    @ExceptionHandler({IncorrectCredentialsException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<String> IncorrectCredentialsException() {
        logger.debug("IncorrectCredentialsException");
        BaseResult<String> result = new BaseResult<String>();
        result.setStatus("401.1");
        result.setMsg("user password not correct");
        return result;
    }

    /**
     * login ok
     * user exist and password correct but no granted
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<String> UnauthorizedException() {
        logger.debug("UnauthorizedException");
        BaseResult<String> result = new BaseResult<String>();
        result.setStatus("401.3");
        result.setMsg("login but no granted");
        return result;
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<String> AuthenticationException() {
        logger.debug("AuthenticationException");
        BaseResult<String> result = new BaseResult<String>();
        result.setStatus("401.2");
        result.setMsg("unexpected AuthenticationException");
        return result;
    }
}
