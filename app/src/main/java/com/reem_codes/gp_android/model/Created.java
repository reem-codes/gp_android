package com.reem_codes.gp_android.model;

public class Created<E> extends Base{

    private E object;


    public Created(E object) {
        this.object = object;
    }

    public E getObject() {
        return object;
    }


    public void setData(E data) {
        this.object = data;
    }

}
