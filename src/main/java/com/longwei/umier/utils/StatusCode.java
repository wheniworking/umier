package com.longwei.umier.utils;

public enum  StatusCode {

    SUCCESS(200,"ok"),
    REQUEST_PARAMS_NOT_VALID(40004,"invalid request param"),
    AUTH_ERROR(40006, "Invalid username or password");

     int statusCode;
     String message;

    StatusCode(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
