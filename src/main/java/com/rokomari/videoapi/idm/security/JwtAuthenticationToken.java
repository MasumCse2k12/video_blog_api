package com.rokomari.videoapi.idm.security;

import com.rokomari.videoapi.common.utils.Utils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final String AUTHORITIES = "ats";
    private static final String STATUS = "ss";;
    private static final String SUBJECT_NAME="sn";


    private JwtModel jwtModel;

    private JwtAuthenticationToken(String subjectName,int userId, Collection<? extends GrantedAuthority> authorities, int status) {
        super(authorities);
        jwtModel = new JwtModel(subjectName,userId,status);
    }

    @Override
    public Object getDetails() {
        return this.jwtModel;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Long getPrincipal() {
        if(jwtModel == null) {
            return null;
        }
        return new Long(jwtModel.getUserId());
    }

    public static JwtAuthenticationToken of(Claims claims) {
        Collection<GrantedAuthority> authorities =
                Arrays.stream(String.valueOf(claims.get(AUTHORITIES)).split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        JwtAuthenticationToken jwtAuthenticationToken =
                new JwtAuthenticationToken(
                        claims.get(SUBJECT_NAME,String.class),
                        Utils.getIntegerFromObject(claims.getSubject()),
                        authorities,
                        Integer.parseInt(claims.get(STATUS).toString())
                );

        Date now = new Date();
        Date expiration = claims.getExpiration();
        Date notBefore = claims.getIssuedAt();
        jwtAuthenticationToken.setAuthenticated(now.after(notBefore) && now.before(expiration));

        return jwtAuthenticationToken;
    }
}
