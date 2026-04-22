package com.example.oms.member.domain.error;

import com.example.oms.core.error.ErrorCode;

public enum MemberError implements ErrorCode {
    MEMBER_NOT_FOUND("MBR-404-NOT_FOUND", "Member not found", 404),
    ;

    private final String code;
    private final String message;
    private final int    status;

    MemberError(String code, String message, int status) {
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
