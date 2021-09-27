package com.rokomari.videoapi.video.repository;

import com.rokomari.videoapi.video.model.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideosRepository extends JpaRepository<Videos, Integer> {
    Optional<Videos> findByUrl(String url);
}
