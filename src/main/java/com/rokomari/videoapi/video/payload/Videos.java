package com.rokomari.videoapi.video.payload;

import com.rokomari.videoapi.common.enums.ReactionStatus;
import com.rokomari.videoapi.common.utils.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Videos implements Serializable {
    protected Integer id;
    private String url;
    private String description;
    private Integer views;
    private String uploadedAt;
    private Integer uploadedBy;
    private String uploadedUser;
    private ReactionStatus status;
    private List<String> likedUser;
    private List<String> dislikeUser;
    private Integer liked;
    private Integer disliked;
    private Boolean isOwnLike;
    private Boolean isOwnDislike;

    public Videos(Object[] data, boolean isSummary){
        super();
        if(data != null){
            if(isSummary){
                if(Utils.valueExists(data, 0)){
                    this.id = Utils.getIntegerFromObject(data[0]);
                }
                if(Utils.valueExists(data, 1)){
                    this.url = (String) data[1];
                }
                if(Utils.valueExists(data, 2)){
                    this.description = (String) data[2];
                }
                if(Utils.valueExists(data, 3)){
                    this.views = Utils.getIntegerFromObject(data[2]);
                }
                if(Utils.valueExists(data, 4)){
                    this.uploadedAt = Utils.getFormattedDate("", (Timestamp)data[4]);
                }
                if(Utils.valueExists(data, 5)){
                    this.uploadedBy = Utils.getIntegerFromObject(data[5]);
                }
                if(Utils.valueExists(data, 6)){
                    this.liked = Utils.getIntegerFromObject(data[6]);
                }
                if(Utils.valueExists(data, 7)){
                    this.disliked = Utils.getIntegerFromObject(data[7]);
                }
                if(Utils.valueExists(data, 8)){
                    Integer status = Utils.getIntegerFromObject(data[8]);
                    if(Utils.isOk(status)){
                        if(status.intValue() == ReactionStatus.like.getValue().intValue()){
                            this.isOwnLike = true;
                        }else if(status.intValue() == ReactionStatus.dislike.getValue().intValue()){
                            this.isOwnDislike = true;
                        }
                    }
                }
            }else{

            }
        }
    }
}
