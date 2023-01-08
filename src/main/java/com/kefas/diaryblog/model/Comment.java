package com.kefas.diaryblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kefas.diaryblog.model.audit.UserBaseClass;
import com.kefas.diaryblog.model.user.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends UserBaseClass {

    @NotNull(message = "Name cannot be empty")
    private String name;

    @Email(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Post body must not be empty")
    private String body;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users users;

    public Comment(String body) {
        this.body = body;
    }
}
