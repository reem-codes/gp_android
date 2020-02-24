package com.reem_codes.gp_android.model;

public class _Response extends Base{
    private boolean isRead;
    private String updateAt;
    private  String executionTime;
    private int id;
    private  boolean isDone;

    public _Response() {

    }

    public _Response(boolean isRead, String updateAt, String executionTime, int id, boolean isDone) {
        this.isRead = isRead;
        this.updateAt = updateAt;
        this.executionTime = executionTime;
        this.id = id;
        this.isDone = isDone;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}
