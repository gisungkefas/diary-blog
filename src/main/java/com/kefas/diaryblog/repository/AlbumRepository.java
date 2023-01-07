package com.kefas.diaryblog.repository;

import com.kefas.diaryblog.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.awt.print.Pageable;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Page<Album> findByCreatedBy(Long id, Pageable pageable);
}
