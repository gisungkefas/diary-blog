package com.kefas.diaryblog.controller;

import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.payload.PostPayload;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.response.PostResponse;
import com.kefas.diaryblog.security.CurrentUser;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {


    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Post>> getAllPosts(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "size", required = false) Integer size) {
        PagedResponse<Post> response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<PagedResponse<Post>> getPostByCategory(@RequestParam(value = "page", required = false) Integer page,
                                                                 @RequestParam(value = "size", required = false) Integer size,
                                                                 @PathVariable(name = "id") Long id) {
        PagedResponse<Post> response = postService.getPostsByCategory(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<PagedResponse<Post>> getPostByTag(@RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "size", required = false) Integer size,
                                                            @PathVariable(name = "id") Long id) {
        PagedResponse<Post> response = postService.getPostsByTag(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> addPost(@Valid @RequestBody PostPayload postPayload,
                                                @CurrentUser UserPrincipal currentUser) {
        PostResponse postResponse = postService.addPost(postPayload, currentUser);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable(name = "id") Long id) {
        Post post = postService.getPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "id") Long id, @Valid @RequestBody PostPayload postPayload,
                                           @CurrentUser UserPrincipal currentUser){
        Post post = postService.updatePost(id, postPayload, currentUser);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deletePost(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = postService.deletePost(id, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}