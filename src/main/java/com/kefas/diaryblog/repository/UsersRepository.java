package com.kefas.diaryblog.repository;

import com.kefas.diaryblog.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    default Users getUsersByName(String email){
        return findByEmail(email).orElseThrow(()-> new ResourceNotFoundEception("Email was not found"));

    default Users getUsers(UserPrincipal currentUser){
        return getUsersByName(currentUser.getEmail());
        }
    }
}