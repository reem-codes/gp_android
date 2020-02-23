package com.reem_codes.gp_android.model;

public class Schedule extends Base{
    private int id;
    private int days;
    private String time;
    private String updateAt;

    public Schedule(int id, int days, String time, String updateAt) {
        this.id = id;
        this.days = days;
        this.time = time;
        this.updateAt = updateAt;
    }

    public Schedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
