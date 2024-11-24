package com.kyonggi.teampu.global.response;

import com.kyonggi.teampu.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private Status status;
    private T body;

    public static <T> ApiResponse<T> ok(T body) {
        return new ApiResponse<>(
                new Status(HttpStatus.OK, "올바른 요청입니다."),
                body
        );
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(
                new Status(HttpStatus.OK, "올바른 요청입니다."),
                null
        );
    }

    public static <T> ApiResponse<T> exception(ErrorCode errorCode) {
        return new ApiResponse<>(
                new Status(errorCode.getHttpStatus(), errorCode.getMessage()),
                null
        );
    }

    @Getter
    @AllArgsConstructor
    public static class Status {
        private HttpStatus httpStatus;
        private String message;
    }
}
