package com.kefas.diaryblog.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kefas.diaryblog.model.audit.UserBaseClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends UserBaseClass {

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Post> posts;
}
