package com.rokomari.videoapi.idm.service;

import com.rokomari.videoapi.common.enums.AccountStatus;
import com.rokomari.videoapi.common.utils.Utils;
import com.rokomari.videoapi.idm.payload.LoginRequest;
import com.rokomari.videoapi.idm.payload.LoginResponse;
import com.rokomari.videoapi.idm.payload.User;
import com.rokomari.videoapi.idm.security.JwtTokenProvider;
import com.rokomari.videoapi.idm.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RedisService redisService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    public LoginResponse login(LoginRequest loginRequest) {
        if(loginRequest == null){
            return new LoginResponse(false, "Empty Request!");
        }
        if(!Utils.isOk(loginRequest.getUsername())){
            return new LoginResponse(false, "Empty User!");
        }
        if(!Utils.isOk(loginRequest.getPassword())){
            return new LoginResponse(false, "Empty Password!");
        }
        try {

            User user = userService.getUserDetails(loginRequest.getUsername());
            if (Utils.isOk(user)) {
                if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    return new LoginResponse(false, "Password not matched!");
                }
            } else {
                return new LoginResponse(false, "UserName not matched!");
            }
        }catch (Throwable t){
            return new LoginResponse(false, "Internal Server Error. Please try again later!");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Integer userStatus = 0;

        if (authentication.getPrincipal() instanceof UserPrincipal) {

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


            if (Utils.isOk(userPrincipal.getStatus())) {
                userStatus = userPrincipal.getStatus();
                if (userStatus == AccountStatus.inactive.getValue()) {
                    return new LoginResponse(false, "This user id is inactive. Please contact with the administrator");
                }

            } else {
                return new LoginResponse(false, "Invalid account status for Provided user id. Please contact with the administrator");
            }


        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date issuedAt = new Date();
        Date expiryDate = new Date(issuedAt.getTime() + jwtExpirationInMs);

        String jwt = tokenProvider.generateToken(authentication, issuedAt, expiryDate);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Integer userId = principal.getId();
        userStatus = principal.getStatus();
        String name = principal.getName();


        try {
            redisService.setValue(String.format("%s", jwt), "ok", TimeUnit.MILLISECONDS, jwtExpirationInMs);
        } catch (Throwable t) {
            t.printStackTrace();
            return new LoginResponse(false, "Memory storage(Redis) is not accessible. Please try again later!");
        }

        LoginResponse response;
        if (userStatus == AccountStatus.active.getValue()) {
            response = new LoginResponse(loginRequest.getUsername(), jwt, userId, name, jwtExpirationInMs, userStatus);
            response.setMessage("Login Success!");
        } else {
            return new LoginResponse(false, "Invalid account status for Provided user id. Please contact with the administrator");
        }

        return response;
    }

}
