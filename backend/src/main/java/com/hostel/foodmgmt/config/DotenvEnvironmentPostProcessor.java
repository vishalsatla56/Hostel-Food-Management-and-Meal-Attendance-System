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
        Map<String, Object> props = new HashMap<>();

        // ── 1. Map system environment variables (works in Docker / Render / CI) ──
        mapEnvVars(System.getenv(), props);

        // ── 2. Overlay with .env file if present (local development) ─────────────
        File[] candidateFiles = new File[]{
                new File(".env"),
                new File("../.env"),
                new File("c:/mess/.env")
        };

        for (File f : candidateFiles) {
            if (f.exists() && f.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                    Map<String, String> fileVars = new HashMap<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eqIdx = line.indexOf('=');
                        if (eqIdx > 0) {
                            String key = line.substring(0, eqIdx).trim();
                            String value = line.substring(eqIdx + 1).trim();
                            if ((value.startsWith("\"") && value.endsWith("\""))
                                    || (value.startsWith("'") && value.endsWith("'"))) {
                                value = value.substring(1, value.length() - 1);
                            }
                            fileVars.put(key, value);
                        }
                    }
                    mapEnvVars(fileVars, props);
                    logger.info("Loaded environment from {}", f.getAbsolutePath());
                } catch (IOException e) {
                    logger.warn("Could not read .env file: {}", e.getMessage());
                }
                break; // use first found file only
            }
        }

        if (!props.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("hostelEnvProperties", props));
        }
    }

    /** Maps well-known env var names to their Spring property equivalents. */
    private void mapEnvVars(Map<String, ?> source, Map<String, Object> target) {
        for (Map.Entry<String, ?> entry : source.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            // Always store the raw key so ${MONGODB_URI} in application.properties resolves
            target.put(key, value);
            // Also map to canonical Spring property names
            switch (key) {
                case "MONGODB_URI":
                    target.put("spring.data.mongodb.uri", value);
                    break;
                case "JWT_SECRET":
                    target.put("app.jwt.secret", value);
                    break;
                case "JWT_EXPIRATION_MS":
                    target.put("app.jwt.expiration-ms", value);
                    break;
                case "CORS_ALLOWED_ORIGINS":
                    target.put("app.cors.allowed-origins", value);
                    break;
                case "PORT":
                    target.put("server.port", value);
                    break;
            }
        }
    }
}
