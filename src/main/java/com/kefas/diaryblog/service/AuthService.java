package com.kefas.diaryblog.service;


import com.kefas.diaryblog.payload.LoginRequest;
import com.kefas.diaryblog.payload.SignUpPayload;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.LoginResponse;
import com.kefas.diaryblog.response.UserRegistrationResponse;

public interface AuthService {

    UserRegistrationResponse registerUser(SignUpPayload signUpRequest);

    ApiResponse<LoginResponse> authenticateUser(LoginRequest loginRequest);
}
