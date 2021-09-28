package com.rokomari.videoapi.idm.service;

import com.rokomari.videoapi.common.payload.IdmException;
import com.rokomari.videoapi.common.utils.Utils;
import com.rokomari.videoapi.idm.payload.User;
import com.rokomari.videoapi.idm.payload.UserResponse;
import com.rokomari.videoapi.idm.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserDetails(String userId) {
        if (!Utils.isOk(userId)) {
            return null;
        }
        Optional<com.rokomari.videoapi.idm.model.User> userEo = userRepository.findUserByUsername(userId);
        if (userEo.isPresent()){
            User userDto = new User(userEo.get());
//            BeanUtils.copyProperties(userEo.get(),userDto);
            return userDto;
        }
        return null;
    }

    @Transactional
    public UserResponse createUser(com.rokomari.videoapi.idm.payload.User userBO) throws IdmException {
        Object[] results = new Object[2];

        com.rokomari.videoapi.idm.model.User userEO = userRepository.findByUsername(userBO.getUsername());
        if (userEO != null) {
            if (userBO.getUsername().trim().equalsIgnoreCase(userEO.getUsername())) {
                throw new IdmException("Username already exists!");
            }
        }
        if(Utils.isOk(userBO.getEmail())) {
            userEO = userRepository.findByEmail(userBO.getEmail());
            if (userEO != null) {
                if (userBO.getEmail().trim().equalsIgnoreCase(userEO.getEmail())) {
                    throw new IdmException("Email already exists!");
                }
            }
        }
        if(Utils.isOk(userBO.getPhone())) {
            userEO = userRepository.findByPhone(userBO.getPhone());
            if (userEO != null) {
                if (userBO.getPhone().trim().equalsIgnoreCase(userEO.getPhone())) {
                    throw new IdmException("Phone number already exists!");
                }
            }
        }
        String passwordHash = null;
        try {
            passwordHash = passwordEncoder.encode(userBO.getPassword());
        } catch (Exception var8) {
            throw new IdmException(var8.getMessage());
        }
        userEO = new com.rokomari.videoapi.idm.model.User();

        if (getUserDetails(userBO.getUsername()) != null) {
            results[1] = "User Already exists in user storage";
            return new UserResponse(results);
        }
        BeanUtils.copyProperties(userBO, userEO);

        try {
            userEO.setPassword(passwordHash);
            com.rokomari.videoapi.idm.model.User savedUser = userRepository.save(userEO);

            results[0] = new User(userRepository.findById(savedUser.getId()).get());
        } catch (Throwable var7) {
            throw new IdmException(var7);
        }
        results[1] = "SUCCESS";
        return new UserResponse(results);

    }

}
