package com.longwei.umier.utils;

public enum  StatusCode {

    SUCCESS(200,"ok"),
    REQUEST_PARAMS_NOT_VALID(40004,"invalid request param");

     int statusCode;
     String message;

    StatusCode(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }
}
