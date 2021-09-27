package com.rokomari.videoapi.idm.security;


import com.rokomari.videoapi.common.utils.ResourceNotFoundException;
import com.rokomari.videoapi.idm.model.User;
import com.rokomari.videoapi.idm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        return this.loadUserByUserId(userId);
    }

    @Transactional
    public UserDetails loadUserByUserId(String userId) {
        Optional<User> user = userRepository.findUserByUsername(userId);
        if(!user.isPresent()) {
            throw new ResourceNotFoundException("User", "user id", userId);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_Common"));
        return UserPrincipal.create(user.get(), authorities);
    }



}