package com.kefas.diaryblog.service.serviceImpl;

import com.kefas.diaryblog.exception.BlogApiException;
import com.kefas.diaryblog.exception.ResourceNotFoundException;
import com.kefas.diaryblog.model.Comment;
import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.model.role.Role;
import com.kefas.diaryblog.model.user.Users;
import com.kefas.diaryblog.payload.CommentPayload;
import com.kefas.diaryblog.repository.CommentRepository;
import com.kefas.diaryblog.repository.PostRepository;
import com.kefas.diaryblog.repository.UsersRepository;
import com.kefas.diaryblog.response.ApiResponse;
import com.kefas.diaryblog.response.PagedResponse;
import com.kefas.diaryblog.security.UserPrincipal;
import com.kefas.diaryblog.service.CommentService;
import com.kefas.diaryblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private UsersRepository usersRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UsersRepository usersRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public PagedResponse<Comment> getAllComments(Long postId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "CreatedAt");

        Page<Comment> comments = commentRepository.findByPostId(postId, (java.awt.print.Pageable) pageable);

        return new PagedResponse<>(comments.getContent(), comments.getNumber(), comments.getSize(), comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
    }

    @Override
    public Comment addComment(CommentPayload commentRequest, Long postId, UserPrincipal currentUser) {

        Users users = usersRepository.getUsers(currentUser);

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post Id was not found")
        );

        Comment comment = new Comment(commentRequest.getBody());
        comment.setUsers(users);
        comment.setPost(post);
        comment.setEmail(currentUser.getEmail());

        return commentRepository.save(comment);
    }

    @Override
    public Comment getComment(Long postId, Long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post Id was not found")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Comment Id was not found")
        );
        if(comment.getPost().getId().equals(post.getId())){

            return comment;
        } else {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment dose not belong to the following post");
        }
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentPayload commentRequest, UserPrincipal currentUser) {

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post Id was not found")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Comment Id was not found")
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        if(comment.getUsers().getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))){
            comment.setBody(commentRequest.getBody());

            return commentRepository.save(comment);
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, "You do not have permission to edit this comment");
    }

    @Override
    public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post Id was not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment Id was not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            return new ApiResponse("Comment does not belong to this post", LocalDateTime.now());
        }

        if (comment.getUsers().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
            commentRepository.deleteById(comment.getId());
            return new ApiResponse("You have successfully deleted a comment", LocalDateTime.now());
        }

        throw new BlogApiException(HttpStatus.UNAUTHORIZED, "You don't have permission to delete this comment");
    }
}
