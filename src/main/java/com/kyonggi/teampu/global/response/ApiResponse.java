package com.kyonggi.teampu.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private Status status;
    private List<T> results;

    public static <T> ApiResponse<T> ok(List<T> results) {
        return new ApiResponse<>(
                new Status(HttpStatus.OK, "OK"),
                new Metadata(results.size()),
                results
        );
    }

    public static <T> ApiResponse<T> ok(T body) {
        return new ApiResponse<>(
                new Status(HttpStatus.OK, "OK"),
                new Metadata(1),
                List.of(body)
        );
    }

    @Getter
    @AllArgsConstructor
    private static class Status {
        private HttpStatus httpStatus;
        private String message;
    }
}
