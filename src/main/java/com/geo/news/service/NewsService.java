package com.geo.news.service;

import com.geo.news.model.CountryInfo;
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
public class NewsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gnews.key}")
    private String apiKey;

    public NewsService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<CountryInfo.NewsItem> getNews(String countryCode) {
        List<CountryInfo.NewsItem> newsList = new ArrayList<>();

        try {
            String url = "https://gnews.io/api/v4/top-headlines"
                    + "?country=" + countryCode.toLowerCase()
                    + "&max=5"
                    + "&apikey=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GeoNews/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode articles = root.path("articles");

            for (JsonNode article : articles) {
                CountryInfo.NewsItem item = new CountryInfo.NewsItem();
                item.setTitle(article.path("title").asText("—"));
                item.setDescription(article.path("description").asText("—"));
                item.setUrl(article.path("url").asText(""));
                item.setPublishedAt(article.path("publishedAt").asText(""));
                item.setSource(article.path("source").path("name").asText("—"));
                item.setImage(article.path("image").asText(""));
                newsList.add(item);
            }

        } catch (Exception e) {
            System.out.println("GNews Error: " + e.getMessage());
        }

        return newsList;
    }
}