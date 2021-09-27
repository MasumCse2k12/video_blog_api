package com.rokomari.videoapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * @author Masum
 */
@Component
@PropertySources({@PropertySource("classpath:application.properties")})
public class BaseRestTemplate {

    @Autowired
    private Environment env;

    private static String PROTOCOL;
    public static String HOST;
    public static String PORT;
    public static String PATH = "/videoapi";


    @PostConstruct
    void init() {

    }

}
