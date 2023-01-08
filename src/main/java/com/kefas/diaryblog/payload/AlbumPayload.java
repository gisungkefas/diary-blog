package com.kefas.diaryblog.payload;

import com.kefas.diaryblog.model.user.Users;
import lombok.Data;

@Data
public class AlbumPayload extends UserDateAuditPayload{

    private Long id;

    private String title;

    private Users users;

    private String photo;
}
