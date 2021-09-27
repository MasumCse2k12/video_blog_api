package com.rokomari.videoapi.video.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rokomari.videoapi.common.enums.ReactionStatus;
import com.rokomari.videoapi.idm.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "video_request")
@NoArgsConstructor
public class VideoRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(name = "video_id")
    private Integer videoId;
    @Column(name = "reaction_status")
    @Convert(converter= ReactionStatus.Converter.class)
    private ReactionStatus status;
    @Column(name = "created_at")
    Timestamp createdAt;
    @Column(name = "updated_at")
    Timestamp updatedAt;
    @Column(name = "reaction_by")
    Integer reactionBy;
}
