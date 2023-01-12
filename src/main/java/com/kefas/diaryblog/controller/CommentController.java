//package com.kefas.diaryblog.controller;
//
//import com.kefas.diaryblog.model.Comment;
//import com.kefas.diaryblog.payload.CommentPayload;
//import com.kefas.diaryblog.response.ApiResponse;
//import com.kefas.diaryblog.response.PagedResponse;
//import com.kefas.diaryblog.security.CurrentUser;
//import com.kefas.diaryblog.security.UserPrincipal;
//import com.kefas.diaryblog.service.CommentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/posts/{postId}/comments")
//public class CommentController {
//
//    private CommentService commentService;
//
//    @Autowired
//    public CommentController(CommentService commentService) {
//        this.commentService = commentService;
//    }
//
//    @GetMapping
//    public ResponseEntity<PagedResponse<Comment>> getAllComments(@PathVariable(name = "postId") Long postId,
//                                                                 @RequestParam(name = "page") Integer page,
//                                                                 @RequestParam(name = "size") Integer size) {
//        PagedResponse<Comment> allComments = commentService.getAllComments(postId, page, size);
//        return new ResponseEntity<>(allComments, HttpStatus.OK);
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<Comment> addComment(@Valid @RequestBody CommentPayload commentPayload, @PathVariable(name = "postId") Long postId,
//                                              @CurrentUser UserPrincipal currentUser) {
//        Comment newComment = commentService.addComment(commentPayload, postId, currentUser);
//        return new ResponseEntity<>(newComment, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Comment> getComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id) {
//        Comment comment = commentService.getComment(postId, id);
//        return new ResponseEntity<>(comment, HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<Comment> updateComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "id") Long id,
//                                                 @Valid @RequestBody CommentPayload commentPayload, @CurrentUser UserPrincipal currentUser) {
//        Comment comment = commentService.updateComment(postId, id, commentPayload, currentUser);
//        return new ResponseEntity<>(comment, HttpStatus.OK);
//    }
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse> deleteComment(@PathVariable(name = "postId") Long postId,
//                                                     @PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
//        ApiResponse apiResponse = commentService.deleteComment(postId, id, currentUser);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
//}
