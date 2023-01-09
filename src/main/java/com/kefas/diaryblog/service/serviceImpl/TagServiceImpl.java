package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.exception.UnauthorizedException;
import com.kefas.diaryblog.model.Tag;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.repository.TagRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.TagService;
import com.kefas.diaryblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public PagedResponse<Tag> getAllTags(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.Direction.DESC, "Create At");

        Page<Tag> tag = tagRepository.findAll(pageable);

        List<Tag> content = tag.getNumberOfElements() == 0 ? Collections.emptyList() : tag.getContent();

        return new PagedResponse<>(content, tag.getNumber(), tag.getSize(), tag.getTotalElements(), tag.getTotalPages(), tag.isLast());
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Tag Id could not be found"));
    }

    @Override
    public Tag addTag(Tag tag, UserPrincipal currentUser) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser) {

        Tag tag = tagRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Tag Id could not be found")
        );
        if(tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){
            tag.setName(newTag.getName());
            return tagRepository.save(tag);
        }

        ApiResponse apiResponse = new ApiResponse("You don't have permission to perform this Operation", LocalDateTime.now());

        throw  new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {

        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag id could not be found"));
        if (tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            tagRepository.deleteById(id);
            return new ApiResponse("You have successfully deleted a tag", LocalDateTime.now());
        }

        ApiResponse apiResponse = new ApiResponse("You don't have permission to delete this tag", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
    }
}
