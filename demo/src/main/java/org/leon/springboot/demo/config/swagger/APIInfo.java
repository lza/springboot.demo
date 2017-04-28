package org.leon.springboot.demo.config.swagger;

/**
 * Created by leon on 2016/10/11.
 */
public class APIInfo {
    private String title;
    private String description;
    private String serviceTerms;
    private String license;
    private String licenseUrl;
    private String version;
    private ContactInfo contact;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceTerms() {
        return serviceTerms;
    }

    public void setServiceTerms(String serviceTerms) {
        this.serviceTerms = serviceTerms;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }
}
