package com.example.oms.core.response;

import com.example.oms.core.response.code.CommonSuccess;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String  code;
    private final String  message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, CommonSuccess.OK.code(), CommonSuccess.OK.message(), result);
    }

    public static <T> ApiResponse<T> onCreateSuccess(T result) {
        return new ApiResponse<>(true, CommonSuccess.CREATED.code(), CommonSuccess.CREATED.message(), result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
