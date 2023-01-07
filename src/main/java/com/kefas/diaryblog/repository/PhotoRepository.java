package com.kefas.diaryblog.repository;

import com.kefas.diaryblog.model.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.awt.print.Pageable;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Page<Photo> findByAlbumId(Long albumId, Pageable pageable);
}
