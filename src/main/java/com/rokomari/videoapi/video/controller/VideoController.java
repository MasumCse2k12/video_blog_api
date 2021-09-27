package com.rokomari.videoapi.video.controller;

import com.rokomari.videoapi.common.payload.ApiResponse;
import com.rokomari.videoapi.video.payload.VideoRequest;
import com.rokomari.videoapi.video.payload.VideosResponse;
import com.rokomari.videoapi.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping(path = "/add")
    public @ResponseBody
    ApiResponse uploadVideo(@RequestBody(required = true) VideoRequest request) {
        return videoService.uploadVideo(request);
    }

    @PostMapping(path = "/viewCount")
    public @ResponseBody
    ApiResponse videosViewCount(@RequestBody(required = true) VideoRequest request) {
        return videoService.videosViewCount(request);
    }

    @PostMapping(path = "/reactVideos")
    public @ResponseBody
    ApiResponse reactVideos(@RequestBody(required = true) VideoRequest request) {
        return videoService.reactVideos(request);
    }

    @PostMapping(path = "/summary")
    public @ResponseBody
    VideosResponse getVideosList(@RequestBody(required = true) VideoRequest request) {
        return videoService.getVideosList(request);
    }

    @PostMapping(path = "/details")
    public @ResponseBody
    VideosResponse getVideosDetails(@RequestBody(required = true) VideoRequest request) {
        return videoService.getVideosDetails(request);
    }
}
