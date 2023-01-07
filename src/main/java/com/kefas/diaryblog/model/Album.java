package com.kefas.diaryblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kefas.diaryblog.model.audit.UserBaseClass;
import com.kefas.diaryblog.model.user.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album extends UserBaseClass {

    @Column(name = "title")
    private String title;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users users;

    @JsonManagedReference
    @OneToMany(mappedBy = "album")
    private List<Photo> photo;
}
