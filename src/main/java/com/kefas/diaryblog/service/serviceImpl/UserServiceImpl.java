package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.AccessDeniedException;
import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.exception.UnauthorizedException;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.model.user.Address;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.*;
import com.kefas.diaryblog.repository.PostRepository;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;

    private PostRepository postRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserSummaryPayload getCurrentUser(UserPrincipal currentUser) {
        return new UserSummaryPayload(currentUser.getId(), currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName());
    }

    @Override
    public Users addUser(UserPayload user) {

        Users newUser = user.getUserFromPayload();

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(newUser.getRole());
        return usersRepository.save(newUser);
    }

    @Override
    public ApiResponse deleteUser(String email, UserPrincipal currentUser) {

        Users users = usersRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User Email could not be found")
        );
        if(!users.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){
            ApiResponse apiResponse = new ApiResponse("You don't have permission to perform this operation "+ email, LocalDateTime.now());
            throw new AccessDeniedException(apiResponse);
        }
        usersRepository.deleteById(users.getId());
        return new ApiResponse("You have successfully deleted the profile of: "+ email, LocalDateTime.now());
    }

    @Override
    public Users updateUser(Users newUser, String email, UserPrincipal currentUser) {

        Users users = usersRepository.getUsersByName(email);
        if(users.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){

            users.setFirstName(newUser.getFirstName());
            users.setLastName(newUser.getLastName());
            users.setPassword(newUser.getPassword());
            users.setAddress(newUser.getAddress());
            users.setPhoneNumber(newUser.getPhoneNumber());
            users.setWebSite(newUser.getWebSite());
            return usersRepository.save(users);
        }
        ApiResponse apiResponse = new ApiResponse("You don't have permission to update the profile of: "+ email, LocalDateTime.now());
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {

        Boolean isAvailable = !usersRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfilePayload getUserProfile(String email) {

        Users users = usersRepository.getUsersByName(email);

        Long postCount = postRepository.countByCreatedBy(users.getId());

        return new UserProfilePayload(users.getId(), users.getFirstName(), users.getLastName(), Instant.now(), users.getEmail(), users.getAddress(), users.getPhoneNumber(), users.getWebSite(), postCount);
    }

    @Override
    public ApiResponse giveAdmin(String email) {

        Users users = usersRepository.getUsersByName(email);
        users.setRole(Role.ADMIN);
        usersRepository.save(users);
        return new ApiResponse("You gave ADMIN role from: "+ email, LocalDateTime.now());
    }

    @Override
    public ApiResponse removeAdmin(String email) {

        Users users = usersRepository.getUsersByName(email);
        users.setRole(Role.USER);
        usersRepository.save(users);
        return new ApiResponse("You took ADMIN role from: "+ email, LocalDateTime.now());
    }

    @Override
    public UserProfilePayload setOrUpdateInfo(UserPrincipal currentUser, InfoPayload infoRequest) {
        Users users = usersRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Email cannot be found"));
        Address address = new Address(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(), infoRequest.getZipcode(), users);
        if(users.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            users.setAddress(address);
            users.setWebSite(infoRequest.getWebsite());
            users.setPhoneNumber(infoRequest.getPhone());
            Users updatedUser = usersRepository.save(users);

            Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return new UserProfilePayload(updatedUser.getId(), updatedUser.getFirstName(),
                    updatedUser.getLastName(), Instant.now(), updatedUser.getEmail(), updatedUser.getAddress(),
                    updatedUser.getPhoneNumber(), updatedUser.getWebSite(), postCount);
        }
        ApiResponse apiResponse = new ApiResponse("You don't have the permission to update users profile", LocalDateTime.now(), HttpStatus.FORBIDDEN);
        throw new AccessDeniedException(apiResponse);

    }
}