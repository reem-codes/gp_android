package com.reem_codes.gp_android.model;

public class User extends Base{
    private String email;
    private String updateAt;
    private int id;

    public User() {

    }

    public User(String email, String updateAt, int id) {
        this.email = email;
        this.updateAt = updateAt;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
