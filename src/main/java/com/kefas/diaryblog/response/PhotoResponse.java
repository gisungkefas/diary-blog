package com.kefas.diaryblog.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoResponse {

    private Long id;

    private String title;

    private String url;

    private String thumbnailUrl;

    private Long albumId;
}
