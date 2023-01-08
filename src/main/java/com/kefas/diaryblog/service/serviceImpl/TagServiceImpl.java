package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.model.Tag;
import com.kefas.diaryblog.repository.TagRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public PagedResponse<Tag> getAllTags(int page, int size) {
        return null;
    }

    @Override
    public Tag getTag(Long id) {
        return null;
    }

    @Override
    public Tag addTag(Tag tag, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {
        return null;
    }
}
