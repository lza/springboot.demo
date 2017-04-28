package org.leon.springboot.demo.db.model;

import java.util.Date;
import java.util.List;

public class User {
    private Long id;

    private Date created;

    private Date lastModified;

    private Boolean active;

    private String email;

    private String name;

    private String password;

    private Boolean twoFactorAuthActivated;

    private Boolean twoFactorAuthEnabled;

    private String twoFactorAuthKey;

    private String twoFactorAuthTotpUrl;

    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Boolean getTwoFactorAuthActivated() {
        return twoFactorAuthActivated;
    }

    public void setTwoFactorAuthActivated(Boolean twoFactorAuthActivated) {
        this.twoFactorAuthActivated = twoFactorAuthActivated;
    }

    public Boolean getTwoFactorAuthEnabled() {
        return twoFactorAuthEnabled;
    }

    public void setTwoFactorAuthEnabled(Boolean twoFactorAuthEnabled) {
        this.twoFactorAuthEnabled = twoFactorAuthEnabled;
    }

    public String getTwoFactorAuthKey() {
        return twoFactorAuthKey;
    }

    public void setTwoFactorAuthKey(String twoFactorAuthKey) {
        this.twoFactorAuthKey = twoFactorAuthKey == null ? null : twoFactorAuthKey.trim();
    }

    public String getTwoFactorAuthTotpUrl() {
        return twoFactorAuthTotpUrl;
    }

    public void setTwoFactorAuthTotpUrl(String twoFactorAuthTotpUrl) {
        this.twoFactorAuthTotpUrl = twoFactorAuthTotpUrl == null ? null : twoFactorAuthTotpUrl.trim();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}