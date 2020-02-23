package com.reem_codes.gp_android.model;

public class Login extends Base{
    private String access_token;
    private User user;

    public Login(String access_token, User user) {
        this.access_token = access_token;
        this.user = user;
    }

    public Login() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
