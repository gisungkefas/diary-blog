package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.exception.UnauthorizedException;
import com.kefas.diaryblog.model.Category;
import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.model.Tag;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.PostPayload;
import com.kefas.diaryblog.repository.CategoryRepository;
import com.kefas.diaryblog.repository.PostRepository;
import com.kefas.diaryblog.repository.TagRepository;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.response.PostResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.PostService;
import com.kefas.diaryblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private UsersRepository usersRepository;

    private CategoryRepository categoryRepository;

    private TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UsersRepository usersRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "Created At");

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    @Override
    public PagedResponse<Post> getPostsByCreatedBy(String email, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Users users = usersRepository.getUsersByName(email);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "created At");
        Page<Post> posts = postRepository.findByCreatedBy(users.getId(), (java.awt.print.Pageable) pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    @Override
    public PagedResponse<Post> getPostsByCategory(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id cannot be found"));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "Created At");
        Page<Post> posts = postRepository.findByCategory(category.getId(), (java.awt.print.Pageable) pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());

    }

    @Override
    public PagedResponse<Post> getPostsByTag(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag id could not be found"));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "Created At");

        Page<Post> posts = postRepository.findByTagsIn(Collections.singletonList(tag), (java.awt.print.Pageable) pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());

    }

    @Override
    public Post updatePost(Long id, PostPayload newPostRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post id was not found"));
        Category category = categoryRepository.findById(newPostRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category id was not found"));
        if (post.getUsers().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            post.setTitle(newPostRequest.getTitle());
            post.setBody(newPostRequest.getBody());
            post.setCategory(category);
            return postRepository.save(post);
        }
        ApiResponse apiResponse = new ApiResponse("You don't have permission to edit this post", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post id was not found"));
        if (post.getUsers().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            postRepository.deleteById(id);
            return new ApiResponse("You have successfully deleted post", LocalDateTime.now());
        }

        ApiResponse apiResponse = new ApiResponse("You don't have permission to perform this operation", LocalDateTime.now());

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PostResponse addPost(PostPayload postRequest, UserPrincipal currentUser) {
        Users users = usersRepository.findById(currentUser.getId()).orElseThrow(()-> new ResourceNotFoundException("User Id was not found"));
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category id was not found"));
        List<Tag> tags = tagRepository.findAll();
        Post post = new Post();
        post.setBody(postRequest.getBody());
        post.setTitle(postRequest.getTitle());
        post.setCategory(category);
        post.setUsers(users);
        post.setTag(tags);

        Post newPost = postRepository.save(post);

        PostResponse postResponse = new PostResponse();
        postResponse.setTitle(newPost.getTitle());
        postResponse.setBody(newPost.getBody());
        postResponse.setCategory(newPost.getCategory().getName());

        List<String> tagNames = new ArrayList<>(newPost.getTag().size());
        for (Tag tag : newPost.getTag()) {
            tagNames.add(tag.getName());
        }
        postResponse.setTags(tagNames);
        return postResponse;
    }

    @Override
    public Post getPost(Long id) {
        return null;
    }
}
