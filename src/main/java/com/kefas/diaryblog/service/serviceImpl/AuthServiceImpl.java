package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.LoginRequest;
import com.kefas.diaryblog.payload.SignUpPayload;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.LoginResponse;
import com.kefas.diaryblog.response.UserRegistrationResponse;
import com.kefas.diaryblog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private UsersRepository usersRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UsersRepository usersRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegistrationResponse registerUser(SignUpPayload signUpRequest) {

        String email = signUpRequest.getEmail();

        boolean exitingUser = usersRepository.existsByEmail(email);
        if(!exitingUser){
            Users users = new Users();

            Role role = users.getRole();

            users.setFirstName(signUpRequest.getFirstName().toLowerCase());
            users.setLastName(signUpRequest.getLastName().toLowerCase());
            users.setEmail(signUpRequest.getEmail().toLowerCase());
            users.setAddress(signUpRequest.getAddress());
            users.setPhoneNumber(signUpRequest.getPhoneNumber().toLowerCase());
            users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            users.setRole(role);
            usersRepository.save(users);

            return new UserRegistrationResponse("Registration Successful", LocalDateTime.now());
        }
        return null;
    }

    @Override
    public ApiResponse<LoginResponse> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Users loggedInnUser = usersRepository.findByEmail(loginRequest.getEmail()).get();
        if(loggedInnUser.getRole().equals(Role.USER) || loggedInnUser.getRole().equals(Role.ADMIN)){
            return new ApiResponse<>("Login Successful", LocalDateTime.now(), new LoginResponse(jwtTokenProvider.generateToken(loginRequest.getEmail())));
        }
        return new ApiResponse<>("Unsuccessful due to bad credential", LocalDateTime.now());
    }
}
