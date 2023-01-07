package com.kefas.diaryblog.exception;

import org.springframework.http.ResponseEntity;

public class ResponseEntityErrorException extends RuntimeException{

    private transient ResponseEntity<ApiResponse> apiResponse;

    public ResponseEntityErrorException(ResponseEntity<ApiResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }

    public ResponseEntity<ApiResponse> getApiResponse() {
        return apiResponse;
    }
}
