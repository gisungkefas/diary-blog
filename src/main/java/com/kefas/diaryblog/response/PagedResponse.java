package com.kefas.diaryblog.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private boolean last;

    public List<T> getContent(){
        return content == null ? null : new ArrayList<>(content);
    }

    public final void setContent(List<T> content){
        if(content == null){
        this.content = null;
    } else {
            this.content = Collections.unmodifiableList(content);
        }

    }

    public boolean isLast(){
        return last;
    }
}
