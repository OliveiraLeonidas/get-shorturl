package com.oliveira.shortener.domain.services;

import com.oliveira.shortener.domain.models.ShortUrlDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class UrlExistenceValidator {
    private static final Logger log = LoggerFactory.getLogger(UrlExistenceValidator.class);

    public static boolean isUrlExists(String urlString) {
        try {
            log.debug("Checking if URL exists: {}", urlString);
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            return (responseCode >= 200 && responseCode < 400); // 2xx and 3xx are valid
        } catch (Exception e) {
            log.error("Error while checking URL: {}", urlString, e);
            return false; // URL is invalid or not reachable
        }
    }


}