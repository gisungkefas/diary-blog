package com.kefas.diaryblog.service;

import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.*;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.security.UserPrincipal;

public interface UserService {

    UserSummaryPayload getCurrentUser(UserPrincipal currentUser);

    Users addUser(UserPayload user);

    ApiResponse deleteUser(String email, UserPrincipal currentUser);

    Users updateUser(Users newUser, String email, UserPrincipal currentUser);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfilePayload getUserProfile(String email);

    ApiResponse giveAdmin(String email);

    ApiResponse removeAdmin(String email);

    UserProfilePayload setOrUpdateInfo(UserPrincipal currentUser, InfoPayload infoRequest);

}
