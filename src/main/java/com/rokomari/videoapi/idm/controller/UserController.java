package com.rokomari.videoapi.idm.controller;

import com.rokomari.videoapi.common.payload.IdmException;
import com.rokomari.videoapi.idm.payload.User;
import com.rokomari.videoapi.idm.payload.UserResponse;
import com.rokomari.videoapi.idm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
@Transactional
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/add")
    public @ResponseBody
    Object addNewUser(HttpServletRequest request, @RequestBody(required = true) User user) {
        try {
            return userService.createUser(user);
        } catch (IdmException ex) {
            return new UserResponse(ex.getMessage());
        }
    }
}
