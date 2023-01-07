package com.kefas.diaryblog.model.user;

import com.kefas.diaryblog.model.audit.UserBaseClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends UserBaseClass {

    @NotNull(message = "Firstname cannot be empty")
    private String Name;

    @NotNull(message = "")

}
