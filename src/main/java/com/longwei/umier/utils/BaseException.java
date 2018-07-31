package com.longwei.umier.utils;

public class BaseException extends RuntimeException{
    private int statusCode;
    private String message;

    public BaseException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public BaseException(int statusCode) {
        this.statusCode = statusCode;
    }



}
