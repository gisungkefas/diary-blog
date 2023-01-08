package com.kefas.diaryblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummaryPayload {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;
}
