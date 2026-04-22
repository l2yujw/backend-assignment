package com.example.oms.core.error;

import java.util.Objects;

public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detail;

    public BaseException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BaseException(ErrorCode errorCode, String detail) {
        super(detail != null ? detail : Objects.requireNonNull(errorCode).message());
        this.errorCode = Objects.requireNonNull(errorCode);
        this.detail = detail;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String detail() {
        return detail;
    }

    public String code() {
        return errorCode.code();
    }

    public int status() {
        return errorCode.status();
    }
}
