package com.longwei.umier.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Throwable.class)
    public DataMap exception(HttpServletRequest request, Throwable ex) {
        return handleError(request, ex);
    }

    @ExceptionHandler(BaseException.class)
    public DataMap baseException(BaseException baseExc) {
        return baseExc.toResponse();
    }

    private DataMap handleError(HttpServletRequest request,
                                Throwable ex) {
        BaseException exception = new BaseException(StatusCode.INTERNAL_SERVER_ERROR);
        ex.printStackTrace();
        return exception.toResponse();
    }
}
