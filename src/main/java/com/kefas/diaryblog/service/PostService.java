package com.kefas.diaryblog.service;


import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.payload.PostPayload;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.response.PostResponse;
import com.kefas.diaryblog.security.UserPrincipal;

public interface PostService {

    PagedResponse<Post> getAllPosts(int page, int size);

    PagedResponse<Post> getPostsByCreatedBy(String email, int page, int size);

    PagedResponse<Post> getPostsByCategory(Long id, int page, int size);

    PagedResponse<Post> getPostsByTag(Long id, int page, int size);

    Post updatePost(Long id, PostPayload newPostRequest, UserPrincipal currentUser);

    ApiResponse deletePost(Long id, UserPrincipal currentUser);

    PostResponse addPost(PostPayload postRequest, UserPrincipal currentUser);

    Post getPost(Long id);
}
