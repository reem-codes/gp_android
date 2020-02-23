package com.reem_codes.gp_android.model;

public class Command extends Base{

    private int id;
    private String updateAt ;

    private Schedule schedule;
    private int hardware_id;
    private boolean configuration;

    public Command() {

    }

    public Command(int id, String updateAt, Schedule schedule, int hardware_id, boolean configuration) {
        this.id = id;
        this.updateAt = updateAt;
        this.schedule = schedule;
        this.hardware_id = hardware_id;
        this.configuration = configuration;
    }

    public int getHardware_id() {
        return hardware_id;
    }

    public void setHardware_id(int hardware_id) {
        this.hardware_id = hardware_id;
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

    public boolean isConfiguration() {
        return configuration;
    }

    public void setConfiguration(boolean configuration) {
        this.configuration = configuration;
    }
}
