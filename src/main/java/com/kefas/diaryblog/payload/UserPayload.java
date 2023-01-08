package com.kefas.diaryblog.payload;

import com.kefas.diaryblog.model.user.Address;
import com.kefas.diaryblog.model.user.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPayload {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Address address;

    private String phoneNumber;

    private String website;

    public Users getUserFromPayload(){
        Users users = new Users();
        users.setFirstName(firstName);
        users.setLastName(lastName);
        users.setEmail(email);
        users.setPassword(password);
        users.setAddress(address);
        users.setPhoneNumber(phoneNumber);
        users.setWebSite(website);

        return new Users();
    }
}
