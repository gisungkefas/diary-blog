package com.kefas.diaryblog.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentPayload {

    @NotNull(message = "Comment content Must not be empty")
    private String body;

    private CommentPayload(String body){
        this.body = body;
    }
}
