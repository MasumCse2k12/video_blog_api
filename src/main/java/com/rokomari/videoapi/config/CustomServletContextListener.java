/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rokomari.videoapi.config;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Masum
 */
//@Configuration
public class CustomServletContextListener implements ServletContextListener {

    @Autowired
    private ExecutorService executorService;

    @Override
    public void contextInitialized(ServletContextEvent context) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent context) {
        executorService.shutdown();
    }

}