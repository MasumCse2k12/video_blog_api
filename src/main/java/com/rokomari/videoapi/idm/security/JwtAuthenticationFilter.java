package com.rokomari.videoapi.idm.security;

import com.rokomari.videoapi.common.utils.Utils;
import com.rokomari.videoapi.idm.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    RedisService redisService;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();
            String jwt = getJwtFromRequest(request);
            String clientIp = request.getHeader("Ip-Address");
            LOGGER.info("IP:{}", clientIp);

            if (StringUtils.hasText(jwt) && tokenProvider.validateJWT(jwt)) {
                Object keyPresent = redisService.getValue(String.format("%s", jwt));
                if (keyPresent == null) {
                    SecurityContextHolder.getContext().setAuthentication(null);
                    response.getWriter().write("Your session has expired. Please re-login!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    Authentication auth = tokenProvider.parseJWT(jwt);

                    JwtModel model = (JwtModel) auth.getDetails();
                    Integer status = model.getStatus();

                    UserPrincipal principal = new UserPrincipal(model.getUserId(),
                            model.getName(), model.getSubjectName(), status, jwt);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, auth.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else if (Utils.isOk(jwt) && !tokenProvider.validateJWT(jwt)) {
                response.getWriter().write("Your session has expired. Please re-login!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write("Internal server error. Please try again later!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        filterChain.doFilter(request, response);
    }
}
