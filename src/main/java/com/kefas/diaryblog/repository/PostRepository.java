package com.kefas.diaryblog.repository;

import com.kefas.diaryblog.model.Post;
import com.kefas.diaryblog.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCreatedBy(Long usersId, Pageable pageable);

    Page<Post> findByCategory(Long categoryId, Pageable pageable);

    Page<Post> findByTagsIn(List<Tag> tags, Pageable pageable);

    Long countByCreatedBy(Long usersId);
}
