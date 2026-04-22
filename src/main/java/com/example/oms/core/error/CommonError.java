package com.example.oms.core.error;

public enum CommonError implements ErrorCode {

    REQ_VALIDATION("VAL-400", "요청 값이 올바르지 않습니다.", 400),
    REQ_BAD_INPUT("BAD-400", "잘못된 입력값입니다.", 400),
    ;

    private final String code;
    private final String message;
    private final int status;

    CommonError(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int status() {
        return status;
    }
}
