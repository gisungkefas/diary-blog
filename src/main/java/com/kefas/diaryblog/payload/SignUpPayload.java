package com.kefas.diaryblog.payload;

import com.kefas.diaryblog.model.user.Address;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class SignUpPayload {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;

    private Address address;

    private String phoneNumber;
}
