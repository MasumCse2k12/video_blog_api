package com.rokomari.videoapi.video.repository;

import com.rokomari.videoapi.video.model.VideoRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRequestRepository extends JpaRepository<VideoRequest, Integer> {
    Optional<VideoRequest> findByReactionByAndVideoId(Integer userId, Integer videoId);
}
