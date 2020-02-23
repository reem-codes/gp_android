package com.reem_codes.gp_android.model;

public class Response extends Base{
    private String isRead;
    private String updateAt;
    private  int executionTime;
    private String message;
    private int id;
    private  String isDone;

    public Response() {

    }

    public Response(String isRead, String updateAt, int executionTime, String message, int id, String isDone) {
        this.isRead = isRead;
        this.updateAt = updateAt;
        this.executionTime = executionTime;
        this.message = message;
        this.id = id;
        this.isDone = isDone;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }
}
