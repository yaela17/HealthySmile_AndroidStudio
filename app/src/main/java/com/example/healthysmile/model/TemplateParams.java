package com.example.healthysmile.model;

public class TemplateParams {
    private String user_name;
    private String user_email;
    private String password;
    private String verification_code;

    public TemplateParams(String password, String user_email, String user_name, String verification_code) {
        this.password = password;
        this.user_email = user_email;
        this.user_name = user_name;
        this.verification_code = verification_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
}
