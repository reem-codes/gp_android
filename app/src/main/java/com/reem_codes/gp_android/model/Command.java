package com.reem_codes.gp_android.model;

public class Command {

    private int id;
    private String updateAt ;

    private Schedule schedule;
    private boolean isOn;

    public Command() {

    }


    public Command(int id, String updateAt, Schedule schedule, boolean isOn) {
        this.id = id;
        this.updateAt = updateAt;
        this.schedule = schedule;
        this.isOn = isOn;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
