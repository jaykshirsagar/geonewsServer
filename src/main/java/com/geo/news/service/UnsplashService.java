package com.geo.news.service;

import com.geo.news.exception.HttpRequestErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnsplashService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${unsplash.key}")
    private String apiKey;

    public UnsplashService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<String> getPhotos(String countryName) {
        List<String> photos = new ArrayList<>();

        try {
            String url = "https://api.unsplash.com/search/photos"
                    + "?query=" + countryName.replace(" ", "+")
                    + "&per_page=6"
                    + "&orientation=landscape"
                    + "&client_id=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GeoNews/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            for (JsonNode photo : results) {
                String imageUrl = photo.path("urls").path("regular").asString("");
                if (!imageUrl.isEmpty()) {
                    photos.add(imageUrl);
                }
            }

        } catch (Exception e) {
            throw HttpRequestErrorHandler.toResponseStatus("Unsplash", e);
        }

        return photos;
    }
}