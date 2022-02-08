package com.gesker;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Named;
import jakarta.ws.rs.ApplicationPath;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConfig // JSF
@ApplicationPath("/") // Rest
public class MyApplicationActivator {
    private static final Logger LOGGER = Logger.getLogger(MyApplicationActivator.class.getName());

    @PostConstruct
    private void init() {
        LOGGER.log(Level.INFO, "MyApplicationActivator initialized");
    }
}
