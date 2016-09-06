package com.example.tjliqy.smsgrouphelper.bean;

import java.util.List;

/**
 * Created by tjliqy on 2016/9/2.
 */
public class Bean<T> {

    private int errno; //0为正确，1为错误
    private String errmsg;

    private T data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
