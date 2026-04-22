package com.example.oms.order.domain.error;

import com.example.oms.core.error.ErrorCode;

public enum OrderError implements ErrorCode {
    ORDER_NOT_FOUND("ORD-404-NOT_FOUND", "Order not found", 404),
    INVALID_PRICE("ORD-400-INV_PRICE", "Price must be positive", 400),
    ;

    private final String code;
    private final String message;
    private final int status;

    OrderError(String code, String message, int status) {
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
