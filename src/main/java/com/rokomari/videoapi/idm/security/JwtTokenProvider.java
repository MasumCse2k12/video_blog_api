package com.rokomari.videoapi.idm.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private static final String AUTHORITIES = "ats";
    private static final String STATUS = "ss";
    private static final String SUBJECT_NAME="sn";


    public String generateToken(Authentication authentication, Date issuedAt, Date expiryDate) {

        String subjectName;
        String subject;
        String authorities;
        Integer status;

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        subjectName = userPrincipal.getUsername();
        subject = Integer.toString(userPrincipal.getId());
        status = userPrincipal.getStatus();
        authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replace("ROLE_",""))
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryDate);

        claims.put(AUTHORITIES, authorities);
        claims.put(STATUS, status);
        claims.put(SUBJECT_NAME,subjectName);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public Authentication parseJWT(String jwtToken) throws AuthenticationException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            return JwtAuthenticationToken.of(claims);
        } catch (ExpiredJwtException | SignatureException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    public boolean validateJWT(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
