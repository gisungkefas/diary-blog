package com.kefas.diaryblog.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserRegistrationResponse {

    private String message;

    private LocalDateTime timeStamp;
}
