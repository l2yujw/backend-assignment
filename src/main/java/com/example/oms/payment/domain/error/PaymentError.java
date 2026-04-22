package com.example.oms.payment.domain.error;

import com.example.oms.core.error.ErrorCode;

public enum PaymentError implements ErrorCode {

    PAYMENT_NOT_FOUND("PAY-404-NOT_FOUND", "Payment not found", 404),
    PAYMENT_ALREADY_EXISTS("PAY-409-ALREADY_EXISTS", "Payment already exists", 409);

    private final String code;
    private final String message;
    private final int status;

    PaymentError(String code, String message, int status) {
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
