package com.reem_codes.gp_android.model;

public class Hardware extends Base{
    private int id;
    private String updateAt;
    private String name;
    private String icon;
    private String desc;
    private int gpio;
    private int raspberry_id;
    private boolean status;

    public Hardware(int id, String updateAt, String name, String icon, String desc, int gpio, int raspberry_id, boolean status) {
        this.id = id;
        this.updateAt = updateAt;
        this.name = name;
        this.icon = icon;
        this.desc = desc;
        this.gpio = gpio;
        this.raspberry_id = raspberry_id;
        this.status = status;
    }

    public Hardware() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getGpio() {
        return gpio;
    }

    public void setGpio(int gpio) {
        this.gpio = gpio;
    }

    public int getRaspberry_id() {
        return raspberry_id;
    }

    public void setRaspberry_id(int raspberry_id) {
        this.raspberry_id = raspberry_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
