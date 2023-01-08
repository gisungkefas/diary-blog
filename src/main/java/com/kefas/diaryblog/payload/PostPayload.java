package com.kefas.diaryblog.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Getter
@Setter
public class PostPayload {

    @NotNull(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Post content cannot be empty")
    private String body;

    @NotNull(message = "Category id cannot be empty")
    private Long categoryId;

    private List<String> tags;
}
