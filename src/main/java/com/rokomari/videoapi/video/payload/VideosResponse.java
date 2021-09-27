package com.rokomari.videoapi.video.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideosResponse implements Serializable {
    private List<Videos> videosList;
    private Integer total;
}
