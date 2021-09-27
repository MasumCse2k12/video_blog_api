package com.rokomari.videoapi.idm.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private User user;
    private String message;

    public UserResponse(String message) {
        this.message = message;
    }

    public UserResponse(Object[] results) {
        if (results != null && results.length >= 2) {
            this.user = (User)results[0];
            this.message = (String)results[1];
        }

    }
}
