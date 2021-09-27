package com.rokomari.videoapi.video.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "videos")
@NoArgsConstructor
public class Videos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(name = "url")
    private String url;
    @Column(name = "description")
    private String description;
    @Column(name = "view_count")
    private Integer views;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;
    @Column(name = "uploaded_by")
    private Integer uploadedBy;
}
