package com.rokomari.videoapi.video.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class SearchCriteria implements Serializable {
    private Integer limit;
    private Integer offset;
    private String orderBy;
}
