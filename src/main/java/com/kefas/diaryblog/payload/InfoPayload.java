package com.kefas.diaryblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class InfoPayload {

    @NotNull
    private String street;

    @NotNull
    private String suite;

    @NotNull
    private String city;

    @NotNull
    private String zipcode;

    private  String companyName;

    private String catchPhrase;

    private String website;

    private String phone;

    private String latitude;

    private String longitude;
}
