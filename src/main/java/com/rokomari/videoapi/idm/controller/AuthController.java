package com.rokomari.videoapi.idm.controller;


import com.rokomari.videoapi.idm.payload.LoginRequest;
import com.rokomari.videoapi.idm.payload.LoginResponse;
import com.rokomari.videoapi.idm.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Transactional
@RequestMapping(path = "/auth")
public class AuthController {
    public static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        LoginResponse response = null;
        try {
             response = authService.login(loginRequest);
            System.out.println("$$$$$$$$$$$$$ " + response.getAccessToken());
        }catch (Throwable t){
            LOGGER.error("LOGIN ERROR:", t);
            return new LoginResponse(false, "Internal Server Error. Please try again later!");
        }
        return response;
    }


    @RequestMapping("/logout")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            //sending back to client app
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
