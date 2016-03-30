package com.newideasoft.qualityinspectorhelper;

/**
 * Created by NewIdeaSoft on 2016/3/29.
 */
public class RegistInfo {
    private String number;
    private String name;
    private String password;
    private String company;
    private String organization;

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getCompany() {
        return company;
    }

    public String getOrganization() {
        return organization;
    }

    public RegistInfo() {
    }

    public RegistInfo(String number, String name, String password, String company, String organization) {
        this.number = number;
        this.name = name;
        this.password = password;
        this.company = company;
        this.organization = organization;
    }

}
