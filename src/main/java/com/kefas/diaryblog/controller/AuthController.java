package com.kefas.diaryblog.controller;

import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.payload.LoginRequest;
import com.kefas.diaryblog.payload.SignUpPayload;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.UserRegistrationResponse;
import com.kefas.diaryblog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody SignUpPayload signUpRequest) {
        UserRegistrationResponse userRegistrationResponse = authService.registerUser(signUpRequest);
        return new ResponseEntity<>(userRegistrationResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws ResourceNotFoundException {
        ApiResponse<?> apiResponse = authService.authenticateUser(loginRequest);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}