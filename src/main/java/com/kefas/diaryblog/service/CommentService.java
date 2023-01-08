package com.kefas.diaryblog.service;

import com.kefas.diaryblog.model.Comment;
import com.kefas.diaryblog.payload.CommentPayload;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;

public interface CommentService {
    PagedResponse<Comment> getAllComments(Long postId, int page, int size);

    Comment addComment(CommentPayload commentRequest, Long postId, UserPrincipal currentUser);

    Comment getComment(Long postId, Long id);

    Comment updateComment(Long postId, Long id, CommentPayload commentRequest, UserPrincipal currentUser);

    ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);

}
