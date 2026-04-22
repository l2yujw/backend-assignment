package com.example.oms.platform.web.error;

import com.example.oms.core.error.BaseException;
import com.example.oms.core.error.CommonError;
import com.example.oms.core.error.FieldError;
import com.example.oms.core.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        return ResponseEntity
                .status(HttpStatus.valueOf(ex.status()))
                .body(ApiResponse.onFailure(ex.code(), ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldError>>> handleValidation(MethodArgumentNotValidException ex) {
        var err = CommonError.REQ_VALIDATION;

        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.valueOf(err.status()))
                .body(ApiResponse.onFailure(err.code(), err.message(), errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        var err = CommonError.REQ_BAD_INPUT;

        return ResponseEntity
                .status(HttpStatus.valueOf(err.status()))
                .body(ApiResponse.onFailure(err.code(), ex.getMessage(), null));
    }
}
