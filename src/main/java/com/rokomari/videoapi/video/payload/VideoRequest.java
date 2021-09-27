package com.rokomari.videoapi.video.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class VideoRequest extends SearchCriteria {
    private Integer id;
    private Integer status;
    private Integer userId;
    private String url;
    private String description;
}
