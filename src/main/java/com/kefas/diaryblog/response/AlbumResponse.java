package com.kefas.diaryblog.response;

import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.UserDateAuditPayload;
import lombok.Data;

@Data
public class AlbumResponse extends UserDateAuditPayload {

    private Long id;

    private String title;

    private Users users;

    private String photo;
}
