package com.longwei.umier.utils;

import org.springframework.validation.BindingResult;

public class ResponseBuilder {

    public static final String DATA = "data";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String STATUS_CODE = "statusCode";
    public static final String TRACE = "trace";

    private int code;
    private String message;
    private String trace;
    private DataMap data = new DataMap();


    public ResponseBuilder ok() {
        this.code = StatusCode.SUCCESS.statusCode;
        this.message = StatusCode.SUCCESS.message;
        return this;
    }

    public ResponseBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResponseBuilder error(int code) {
        this.code = code;
        return this;
    }


    public ResponseBuilder error(StatusCode error)

    {
        this.code = error.statusCode;
        this.message = error.message;
        return this;
    }

    public ResponseBuilder message(String message)

    {
        this.message = message == null ? "" : message;
        return this;
    }

    public ResponseBuilder data(Object content)

    {
        this.code = StatusCode.SUCCESS.statusCode;
        this.message = StatusCode.SUCCESS.message;
        data.addAttribute(ResponseBuilder.DATA, content);
        return this;
    }

    public ResponseBuilder data(String field, Object data)

    {
        this.data.put(field, data);
        return this;
    }


    public ResponseBuilder trace(String trace)

    {
        this.trace = trace;
        return this;
    }

    public ResponseBuilder errorData(Object content)

    {
        data.addAttribute(ResponseBuilder.DATA, content);
        return this;
    }

    public DataMap build() {
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(ResponseBuilder.STATUS_CODE, code);
        dataMap.putAll(data);
        if (message != null) {
            dataMap.addAttribute(ResponseBuilder.MESSAGE, message);
        }
        if (code != StatusCode.SUCCESS.statusCode) {
            dataMap.addAttribute(ResponseBuilder.TRACE, trace);
        }
        return dataMap;
    }

    public static ResponseBuilder create() {
        return new ResponseBuilder();
    }

    public static DataMap requestNotValid(BindingResult result) {
        return create().error(StatusCode.REQUEST_PARAMS_NOT_VALID)
                .message(ValidatorUtils.buildErrorMessage(result))
                .build();
    }


}
