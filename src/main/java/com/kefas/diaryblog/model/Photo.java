package com.kefas.diaryblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kefas.diaryblog.model.audit.UserBaseClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Photo extends UserBaseClass {

    private String title;

    private String url;

    private String thumbnailUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;
}
