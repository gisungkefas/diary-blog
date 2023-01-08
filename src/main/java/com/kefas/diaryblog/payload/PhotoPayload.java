package com.kefas.diaryblog.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PhotoPayload {

    @NotNull
    private String title;

    @NotNull
    private String thumbnailUrl;

    @NotNull
    private String url;

    @NotNull
    private Long albumId;
}
