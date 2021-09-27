package com.rokomari.videoapi.idm.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer status;

    public User(com.rokomari.videoapi.idm.model.User userEO){
        if(userEO != null){
            this.id = userEO.getId();
            this.name = userEO.getName();
            this.username = userEO.getUsername();
            this.password = userEO.getPassword();
            this.status = userEO.getStatus();
            this.email = userEO.getEmail();
            this.phone =userEO.getPhone();
        }
    }

}
