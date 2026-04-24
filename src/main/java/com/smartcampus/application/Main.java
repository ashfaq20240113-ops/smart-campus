package com.smartcampus.application;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.net.URI;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    //Add /api/v1 to the base URL
    public static final String BASE_URI = "http://0.0.0.0:8080/api/v1/";

    public static void main(String[] args) throws IOException {
        ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus")
                .register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down Smart Campus API...");
            server.shutdownNow();
        }));

        LOGGER.info("Smart Campus API started at: http://localhost:8080/api/v1/");
        LOGGER.info("Press ENTER to stop...");
        System.in.read();
        server.shutdownNow();
    }
}