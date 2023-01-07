package com.kefas.diaryblog.model.user;

import com.kefas.diaryblog.model.Album;
import com.kefas.diaryblog.model.Comment;
import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.model.audit.UserBaseClass;
import com.kefas.diaryblog.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends UserBaseClass {

    @NotNull(message = "FirstName cannot be empty")
    private String firstName;

    @NotNull(message = "LastName cannot be empty")
    private String lastName;

    @Email
    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "PhoneNumber cannot be empty")
    private String phoneNumber;

    private String webSite;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "users")
    private List<Album> albums;

    @OneToMany(mappedBy = "users")
    private List<Post> posts;

    @OneToMany(mappedBy = "users")
    private List<Comment> comments;

    public Users(Long id, LocalDateTime createDate, LocalDateTime updateDate, Long createdBy, Long updatedBy, String firstName, String lastName, String email, String password, String phoneNumber, String webSite, Role role, Address address, List<Album> albums, List<Post> posts, List<Comment> comments) {
        super(id, createDate, updateDate, createdBy, updatedBy);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        this.role = role;
        this.address = address;
        this.albums = albums;
        this.posts = posts;
        this.comments = comments;
    }
}
