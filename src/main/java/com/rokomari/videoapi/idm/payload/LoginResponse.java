package com.rokomari.videoapi.idm.payload;


import com.rokomari.videoapi.common.payload.ApiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse extends ApiResponse {
    private Integer id;
    private String username;
    private String accessToken;
    private String tokenType = "Bearer";
    private String password;
    private String name;
    private long sessionExpirationInMs;
    Integer status;

    public LoginResponse(boolean success,String message) {
        super(success, message);
    }

    public LoginResponse(String username,String accessToken, Integer id,  String name, long timout, Integer status) {
        super(true);
        this.username = username;
        this.accessToken = accessToken;
        this.id = id;
        this.name = name;
        this.sessionExpirationInMs = timout;
        this.status = status;
    }

}
