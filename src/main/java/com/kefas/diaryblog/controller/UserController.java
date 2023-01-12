package com.kefas.diaryblog.controller;

import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.*;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.CurrentUser;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.AlbumService;
import com.kefas.diaryblog.service.PostService;
import com.kefas.diaryblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    private PostService postService;

    private AlbumService albumService;


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSummaryPayload> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummaryPayload userSummary = userService.getCurrentUser(currentUser);
        return new ResponseEntity<>(userSummary, HttpStatus.OK);
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String email) {
        UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfilePayload> getUserProfile(@PathVariable(value = "username") String username) {
        UserProfilePayload userProfile = userService.getUserProfile(username);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<PagedResponse<Post>> getPostsCreatedBy(@PathVariable(value = "username") String username,
                                                                 @RequestParam(value = "page") Integer page,
                                                                 @RequestParam(value = "size") Integer size) {
        PagedResponse<Post> response = postService.getPostsByCreatedBy(username, page, size);
        return new ResponseEntity<  >(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/albums")
    public ResponseEntity<PagedResponse<Album>> getUserAlbums(@PathVariable(name = "username") String username,
                                                              @RequestParam(name = "page") Integer page,
                                                              @RequestParam(name = "size") Integer size) {
        PagedResponse<Album> response = albumService.getUserAlbums(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Users> addUser(@Valid @RequestBody UserPayload user) {
        Users users = userService.addUser(user);
        return  new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody User newUser,
                                           @PathVariable(value = "email") String email, @CurrentUser UserPrincipal currentUser) {
        Users updatedUser = userService.updateUser(newUser, email, currentUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
                                                  @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = userService.deleteUser(username,currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removeAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.removeAdmin(username);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    public ResponseEntity<UserProfilePayload> setAddress(@CurrentUser UserPrincipal currentUser,
                                                         @Valid @RequestBody InfoPayload infoRequest) {
        UserProfilePayload userProfile = userService.setOrUpdateInfo(currentUser, infoRequest);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
