package com.kefas.diaryblog.payload;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class SignUpPayload {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;
}
