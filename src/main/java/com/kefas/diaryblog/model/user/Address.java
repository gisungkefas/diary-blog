package com.kefas.diaryblog.model.user;

import com.kefas.diaryblog.model.audit.UserBaseClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends UserBaseClass {

    private String street;

    private String country;

    private String city;

    private String zipCode;

    @OneToOne(mappedBy = "address")
    private Users users;
}
