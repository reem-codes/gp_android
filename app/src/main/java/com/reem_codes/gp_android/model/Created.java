package com.reem_codes.gp_android.model;

public class Created<E> {

    private E object;
    private String message;

    public E getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }

    public void setData(E data) {
        this.object = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
