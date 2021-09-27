package com.rokomari.videoapi.idm.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class JwtModel implements Serializable {
    private String subjectName;
    private String name;
    private int userId;
    private int status;

    public JwtModel(String subjectName, int userId, int status) {
        this.subjectName = subjectName;
        this.userId = userId;
        this.status = status;
    }

}
