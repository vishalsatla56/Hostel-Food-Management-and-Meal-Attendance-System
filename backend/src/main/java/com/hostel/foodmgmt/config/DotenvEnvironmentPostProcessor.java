package com.hostel.foodmgmt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DotenvEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        File[] candidateFiles = new File[]{
                new File(".env"),
                new File("../.env"),
                new File("c:/mess/.env")
        };

        File envFile = null;
        for (File f : candidateFiles) {
            if (f.exists() && f.isFile()) {
                envFile = f;
                break;
            }
        }

        if (envFile == null) {
            return;
        }

        Map<String, Object> envVars = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eqIdx = line.indexOf('=');
                if (eqIdx > 0) {
                    String key = line.substring(0, eqIdx).trim();
                    String value = line.substring(eqIdx + 1).trim();
                    if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    envVars.put(key, value);
                    if ("MONGODB_URI".equalsIgnoreCase(key)) {
                        envVars.put("spring.data.mongodb.uri", value);
                    }
                    if ("JWT_SECRET".equalsIgnoreCase(key)) {
                        envVars.put("app.jwt.secret", value);
                    }
                    if ("JWT_EXPIRATION_MS".equalsIgnoreCase(key)) {
                        envVars.put("app.jwt.expiration-ms", value);
                    }
                    if ("PORT".equalsIgnoreCase(key)) {
                        envVars.put("server.port", value);
                    }
                }
            }
            if (!envVars.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", envVars));
            }
        } catch (IOException e) {
            logger.warn("Could not read .env file: {}", e.getMessage());
        }
    }
}
