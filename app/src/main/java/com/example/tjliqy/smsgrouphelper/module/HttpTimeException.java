package com.example.tjliqy.smsgrouphelper.module;

/**
 * Created by tjliqy on 2016/9/5.
 */
public class HttpTimeException extends RuntimeException {

    public  static final int NO_DATA = 0;

    public HttpTimeException(int resultCode){
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage){
        super(detailMessage);
    }

    private static String getApiExceptionMessage(int code){
        String message = "";

        switch (code){
            case NO_DATA:
                message = "无数据";
                break;
            default:
                message = "error";
                break;
        }
        return message;
    }
}
