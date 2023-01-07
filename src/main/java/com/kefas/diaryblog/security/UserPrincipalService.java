package com.kefas.diaryblog.security;

import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserPrincipalService implements UserDetailsService {

    private UsersRepository usersRepository;

    @Autowired
    public void UsersRepositoryService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users users = usersRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFounfException("Email not found"));

        UserPrincipal userPrincipal = null;
        if (users != null){
            userPrincipal = new UserPrincipal(users);
        }

        return userPrincipal;
    }
}
