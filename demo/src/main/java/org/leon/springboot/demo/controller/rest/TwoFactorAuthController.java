package org.leon.springboot.demo.controller.rest;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.leon.springboot.demo.Utils;
import org.leon.springboot.demo.controller.rest.params.BaseResult;
import org.leon.springboot.demo.db.model.User;
import org.leon.springboot.demo.db.service.UserService;
import org.leon.springboot.demo.services.qrcode.QrCodeService;
import org.leon.springboot.demo.services.qrcode.QrStream;
import org.leon.springboot.demo.shiro.ShiroPermissions;
import org.leon.springboot.demo.twoFactorAuth.TwoFactorAuthCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Created by leon on 2017/4/19.
 */
@RestController
@RequestMapping("/api/twoFactorAuth")
public class TwoFactorAuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TwoFactorAuthCfg twoFactorAuthCfg;

    @GetMapping(value = "/getQr")
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.TwoFactorAuthGetQr)
    public HttpEntity getQr() {
        HttpEntity httpEntity = null;

        try {
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);

            boolean enabled = user.getTwoFactorAuthEnabled();
            boolean activated = user.getTwoFactorAuthActivated();
            if(enabled && !activated){
                String otpAuthTotpURL = user.getTwoFactorAuthTotpUrl();
                QrStream qrStream = qrCodeService.createQrInputStreamBase(otpAuthTotpURL);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                httpEntity = new HttpEntity(IOUtils.toByteArray(qrStream.getInputStream()), headers);
            }else {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                BaseResult<String> result = new BaseResult<String>();
                result.setStatus("400");
                result.setMsg("enabled:" + enabled + ",activated:" + activated);
                String body = Utils.convertObjectToJson(result);
                httpEntity = new HttpEntity(body.getBytes(), headers);
            }
        } catch (Exception e){
            logger.error("exception:" + e);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            BaseResult<String> result = new BaseResult<String>();
            result.setStatus("500");
            result.setMsg("exception:" + e);
            String body = Utils.convertObjectToJson(result);
            httpEntity = new HttpEntity(body.getBytes(), headers);
        }

        return httpEntity;
    }

    @GetMapping(value = "/auth/{pin}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.TwoFactorAuthAuth)
    public BaseResult<String> auth(@PathVariable("pin")String pin){
        BaseResult<String> result = new BaseResult<String>();

        try {
            final Subject subject = SecurityUtils.getSubject();
            String email = (String) subject.getPrincipal();
            User user = userService.findByEmailAndActive(email, true);
            logger.debug("user:{}", user.getEmail());

            GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
            boolean valid = googleAuthenticator.authorize(user.getTwoFactorAuthKey(), Integer.parseInt(pin));
            if (valid){
                if(!user.getTwoFactorAuthActivated()){
                    User tempUser = new User();
                    tempUser.setId(user.getId());
                    tempUser.setTwoFactorAuthActivated(true);
                    userService.update(tempUser);
                }
                subject.getSession().setAttribute("isOTPverified", true);

                result.setStatus("200");
            }else {
                result.setStatus("401");
                result.setMsg("two factor auth fail");
            }
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e);
        }
        return result;
    }

    @GetMapping(value = "/reset/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresAuthentication
    @RequiresPermissions(ShiroPermissions.TwoFactorAuthReset)
    public BaseResult<String> reset(@PathVariable("email")User user){
        BaseResult<String> result = new BaseResult<String>();

        try {
            if(null == user){
                result.setStatus("400");
                result.setMsg("user not found");
            }else {
                init(user, false);
                result.setStatus("200");
            }
        } catch (Exception e){
            logger.error("exception:" + e);
            result.setStatus("500");
            result.setMsg("exception:" + e);
        }

        return result;
    }

    public void init(User user, boolean dump){
        String email = user.getEmail();
        logger.debug("user:" + email);
        String otpAuthTotpURL = initTwoFactorAuth(user);
        if (dump){
            QrStream qrStream = null;
            try {
                qrStream = qrCodeService.createQrInputStreamBase(otpAuthTotpURL);
                FileUtils.copyInputStreamToFile(qrStream.getInputStream(), new File("d:/admin.png"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(qrStream.getInputStream());
            }
        }
    }
    private String initTwoFactorAuth(User user){
        String email = user.getEmail();
        logger.debug("current user:" + email);
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
        logger.debug("key:" + googleAuthenticatorKey.getKey());
        String otpAuthTotpURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(twoFactorAuthCfg.getOrganization(), email, googleAuthenticatorKey);
        logger.debug("totp url:" + otpAuthTotpURL);

        User tempUser = new User();
        tempUser.setId(user.getId());
        tempUser.setTwoFactorAuthEnabled(true);
        tempUser.setTwoFactorAuthActivated(false);
        tempUser.setTwoFactorAuthKey(googleAuthenticatorKey.getKey());
        tempUser.setTwoFactorAuthTotpUrl(otpAuthTotpURL);
        userService.update(tempUser);
        return otpAuthTotpURL;
    }
}
