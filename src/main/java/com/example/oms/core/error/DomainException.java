package com.example.oms.core.error;

public class DomainException extends BaseException {

    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DomainException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
