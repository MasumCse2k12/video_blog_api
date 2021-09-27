package com.rokomari.videoapi.config;


import com.rokomari.videoapi.common.utils.AppConstants;
import com.rokomari.videoapi.idm.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.ConnectException;

/**
 *
 * @author Masum
 */
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHeaderInterceptor.class);
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = "Bearer ";
        try {
            if (SecurityContextHolder.getContext() != null
                    && SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {

                try {
                    UserPrincipal up = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (up != null) {
                        token += up.getToken();
                    }
                } catch (Throwable t) {
                     LOGGER.info("Interceptor :: {} ", t.getMessage());
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        request.getHeaders().add(AppConstants.SECURITY_HEADER, token);
        ClientHttpResponse response;
        try {
            response = execution.execute(request, body);

        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof java.net.UnknownHostException) {
                throw new RuntimeException("Connection error occurred. Please try again later.");
            } else if (ex instanceof ResourceAccessException) {
                throw new RuntimeException("Connection error occurred. Please try again later.");
            } else if (ex instanceof ConnectException){
                throw new RuntimeException("Connection error occurred. Please try again later.");
            } else {
                throw new RuntimeException("Internal server error. Please contact with admin!");
            }
        }
        if (response == null) {
            throw new RuntimeException("Connection error occurred. Please try again later.");
        }

        if (response != null && response.getStatusCode().toString().contains("401")) {
            throw new RuntimeException("Un Authorized Action: [401].");
        }

        return response;
    }
}
