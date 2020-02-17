package com.reem_codes.gp_android.model;

public class Raspberry {

    private int id;
    private String name;
    private String updateAt;

    public Raspberry(int id, String name, String updateAt) {
        this.id = id;
        this.name = name;
        this.updateAt = updateAt;
    }

    public Raspberry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}