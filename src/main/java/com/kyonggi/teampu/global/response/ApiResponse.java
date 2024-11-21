package com.kyonggi.teampu.global.response;

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
                new Status(HttpStatus.OK, "OK"),
                body
        );
    }

    public static <T> ApiResponse<T> noContent(T body) {
        return new ApiResponse<>(
                new Status(HttpStatus.NO_CONTENT, "No Content"),
                body
        );
    }

    @Getter
    @AllArgsConstructor
    private static class Status {
        private HttpStatus httpStatus;
        private String message;
    }
}
