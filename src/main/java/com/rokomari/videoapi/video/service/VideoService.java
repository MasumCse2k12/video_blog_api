package com.rokomari.videoapi.video.service;

import com.rokomari.videoapi.common.enums.ReactionStatus;
import com.rokomari.videoapi.common.payload.ApiResponse;
import com.rokomari.videoapi.common.utils.Defs;
import com.rokomari.videoapi.common.utils.Utils;
import com.rokomari.videoapi.idm.security.UserPrincipal;
import com.rokomari.videoapi.video.payload.VideoRequest;
import com.rokomari.videoapi.video.payload.Videos;
import com.rokomari.videoapi.video.payload.VideosResponse;
import com.rokomari.videoapi.video.repository.IBaseRepository;
import com.rokomari.videoapi.video.repository.VideoRequestRepository;
import com.rokomari.videoapi.video.repository.VideosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VideoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    private IBaseRepository repository;
    @Autowired
    private VideosRepository videosRepository;
    @Autowired
    private VideoRequestRepository requestRepository;


    public ApiResponse uploadVideo(VideoRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return new ApiResponse(false, "User must be logged to upload videos!");

            if (request == null) return new ApiResponse(false, "Empty Request!");

            if (!Utils.isOk(request.getUrl())) return new ApiResponse(false, "Video URL is required!");

            Optional<com.rokomari.videoapi.video.model.Videos> videoEO = videosRepository.findByUrl(request.getUrl());
            if (videoEO.isPresent())
                return new ApiResponse(false, "The requested Video URL is already uploaded by another user!");

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            com.rokomari.videoapi.video.model.Videos videoSaved = new com.rokomari.videoapi.video.model.Videos();

            videoSaved.setUrl(request.getUrl());
            videoSaved.setDescription(request.getDescription());
            videoSaved.setIsDeleted(false);
            videoSaved.setUploadedAt(Timestamp.from(Instant.now()));
            videoSaved.setUploadedBy(principal.getId());

            videosRepository.save(videoSaved);
        } catch (Throwable t) {
            LOGGER.error("videos reactions error :: {}", t != null ? t.getMessage() : "NP");
            return new ApiResponse(false, "Internal Server Error");
        }
        return new ApiResponse(true);
    }

    @Transactional
    public ApiResponse videosViewCount(VideoRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return new ApiResponse(false, "User must be logged to upload videos!");

            if (request == null) return new ApiResponse(false, "Empty Request!");

            if (!Utils.isOk(request.getId())) return new ApiResponse(false, "Video ID is required!");

            if (!Utils.isOk(request.getStatus())) return new ApiResponse(false, "Video Reaction status is required!");

            Optional<com.rokomari.videoapi.video.model.Videos> videoEO = videosRepository.findById(request.getId());

            if (!videoEO.isPresent()) return new ApiResponse(false, "Video ID not found!");

            synchronized (this) {
                videoEO.get().setViews(Utils.isOk(videoEO.get().getViews()) ? (videoEO.get().getViews() + 1) : 1);
                videosRepository.save(videoEO.get());
            }


        } catch (Throwable t) {
            LOGGER.error("videos reactions error :: {}", t != null ? t.getMessage() : "NP");
            throw new RuntimeException("Internal Server Error");
        }
        return new ApiResponse(true, "Video view count increased");
    }

    @Transactional
    public ApiResponse reactVideos(VideoRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return new ApiResponse(false, "User must be logged to upload videos!");

            if (request == null) return new ApiResponse(false, "Empty Request!");

            if (!Utils.isOk(request.getId())) return new ApiResponse(false, "Video ID is required!");

            if (!Utils.isOk(request.getStatus())) return new ApiResponse(false, "Video Reaction status is required!");

            Optional<com.rokomari.videoapi.video.model.Videos> videoEO = videosRepository.findById(request.getId());

            if (!videoEO.isPresent()) return new ApiResponse(false, "Video ID not found!");

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            com.rokomari.videoapi.video.model.VideoRequest savedVideo = new com.rokomari.videoapi.video.model.VideoRequest();

            Optional<com.rokomari.videoapi.video.model.VideoRequest> videoRequest = requestRepository.findByReactionByAndVideoId(principal.getId(), request.getId());

            synchronized (this) {
                if (videoRequest.isPresent()) {
                    savedVideo = videoRequest.get();
                    savedVideo.setUpdatedAt(Timestamp.from(Instant.now()));
                } else {
                    savedVideo.setReactionBy(principal.getId());
                    savedVideo.setVideoId(request.getId());
                    savedVideo.setCreatedAt(Timestamp.from(Instant.now()));
                }
                savedVideo.setStatus(ReactionStatus.getReactionStatus(request.getId()));
                requestRepository.save(savedVideo);
            }

        } catch (Throwable t) {
            LOGGER.error("videos reactions error :: {}", t != null ? t.getMessage() : "NP");
            throw new RuntimeException("Internal Server Error");
        }
        return new ApiResponse(true);
    }

    public VideosResponse getVideosList(VideoRequest request) {
        VideosResponse response = new VideosResponse();
        try {
            List<Videos> dataList = new ArrayList<>();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Integer userId = null;
            if (authentication != null) {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                userId = principal.getId();
            }
            String mainQ = "select v.id, v.url, v.description, v.view_count, v.uploaded_at, v.uploaded_by, " +
                    "SUM(CASE WHEN vr.reaction_status =:like THEN 1 ELSE 0 END) AS likeCount," +
                    "SUM(CASE WHEN vr.reaction_status =:dislike THEN 1 ELSE 0 END) AS dislikeCount, CASE WHEN  " + userId + " is not null AND v.id = " + userId + " then vr.reaction_status else 0 end " +
                    "from videos v left join video_request vr on vr.video_id = v.id " +
                    "where v.is_deleted =:isDeleted ";

            Map<String, Object> params = new HashMap<>();
            params.put("like", ReactionStatus.like.getValue());
            params.put("dislike", ReactionStatus.dislike.getValue());
            params.put("isDeleted", false);

            mainQ += "group by v.id, v.url, v.description, v.view_count, v.uploaded_at, v.uploaded_by, vr.reaction_status order by v.id desc";

            LOGGER.info("===videos summary QUERY:{}", mainQ);
            LOGGER.info("===PARAM START==");

            params.forEach((k, v) -> LOGGER.info("==KEY:{},VALUE:{}", k, v));
            LOGGER.info("===PARAM END==");
            Integer limit = Utils.isOk(request.getLimit()) ? request.getLimit() : Defs.LIMIT;
            Integer offset = Utils.isOk(request.getOffset()) ? request.getOffset() : 0;
            List<Object[]> result = repository.findByNativeQuery(mainQ, params, offset, limit);

            LOGGER.info("==RESULT:{}", result);

            if (result != null && result.size() > 0) {
                result.forEach(r -> dataList.add(new Videos(r, true)));
            }
            LOGGER.info("==RETURN REPORT SIZE:{}", dataList.size());
            response.setVideosList(dataList);
            response.setTotal(dataList != null ? dataList.size() : 0);
        } catch (Throwable t) {
            LOGGER.error("videos summary error :: {}", t != null ? t.getMessage() : "NP");
        }
        return response;
    }

    public VideosResponse getVideosDetails(VideoRequest request) {
        VideosResponse response = new VideosResponse();
        try {

            if (request == null || !Utils.isOk(request.getId())) return new VideosResponse();
            String mainQ = "select v.url, u.id, u.name " +
                    " from videos v inner join video_request vr on vr.video_id = v.id left join users u on u.id = vr.reaction_by " +
                    " where  vr.reaction_status =:like ";
            Map<String, Object> params = new HashMap<>();
            params.put("like", ReactionStatus.like.getValue());

            LOGGER.info("===videos details like QUERY:{}", mainQ);
            LOGGER.info("===PARAM START==");

            params.forEach((k, v) -> LOGGER.info("==KEY:{},VALUE:{}", k, v));
            LOGGER.info("===PARAM END==");

            List<Object[]> result = repository.findByNativeQuery(mainQ, params);

            LOGGER.info("==RESULT:{}", result);
            List<String> likedUser = new ArrayList<>();
            AtomicReference<Integer> userId=null;
            AtomicReference<String> url = new AtomicReference<>("");
            if (result != null && result.size() > 0) {
                result.forEach(r -> {
                    if(r != null){
                        if(Utils.valueExists(r, 0)){
                            url.set((String) r[0]);
                        }
                        if(Utils.valueExists(r, 1)){
                            userId.set(Utils.getIntegerFromObject(r[1]));
                        }
                        if(Utils.valueExists(r, 2)){
                            likedUser.add((String) r[2]);
                        }
                    }
                });
            }

            params.clear();

            mainQ = "select v.url, u.id, u.name " +
                    " from videos v inner join video_request vr on vr.video_id = v.id left join users u on u.id = vr.reaction_by " +
                    " where  vr.reaction_status =:like ";

            LOGGER.info("===videos details dislike QUERY:{}", mainQ);
            LOGGER.info("===PARAM START==");
            params.put("dislike", ReactionStatus.dislike.getValue());
            params.forEach((k, v) -> LOGGER.info("==KEY:{},VALUE:{}", k, v));
            LOGGER.info("===PARAM END==");

            result = repository.findByNativeQuery(mainQ, params);

            LOGGER.info("==RESULT:{}", result);
            List<String> dislikedUser = new ArrayList<>();

            if (result != null && result.size() > 0) {
                result.forEach(r -> {
                    if(r != null){
                        if(Utils.valueExists(r, 2)){
                            dislikedUser.add((String) r[2]);
                        }
                    }
                });
            }

            Videos videos = new Videos();
            videos.setLikedUser(likedUser);
            videos.setDislikeUser(dislikedUser);
            videos.setUrl(Utils.isOk(url) ? url.get() : "N/A");
            videos.setUploadedBy(Utils.isOk(userId) ? userId.get() : null);
            response.setVideosList(Arrays.asList(videos));
        } catch (Throwable t) {
            LOGGER.error("videos summary error :: {}", t != null ? t.getMessage() : "NP");
        }
        return response;
    }
}
