package com.example.oms.platform.web.response;

import com.example.oms.core.response.ApiResponse;
import com.example.oms.core.response.code.CommonSuccess;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public final class ApiResponses {

    private ApiResponses() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(T result) {
        return ResponseEntity.status(CommonSuccess.OK.status())
                .body(ApiResponse.onSuccess(result));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(URI location, T result) {
        return ResponseEntity.created(location)
                .body(ApiResponse.onCreateSuccess(result));
    }
}
