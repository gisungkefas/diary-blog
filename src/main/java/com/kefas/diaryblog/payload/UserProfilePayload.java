package com.kefas.diaryblog.payload;

import com.kefas.diaryblog.model.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePayload {

    private Long id;

    private String firstName;

    private String lastName;

    private Instant joinedAt;

    private String email;

    private Address address;

    private String phone;

    private String website;

    public UserProfilePayload(Long id, String username, String firstName, String lastName, Instant now, String email, Address address, String phone, String website, Long postCount) {
    }
}
