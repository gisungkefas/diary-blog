package com.kefas.diaryblog.exception;

import com.kefas.diaryblog.response.ApiResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends RuntimeException{

    private transient ApiResponse apiResponse;

    private String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}
