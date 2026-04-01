package com.geo.news.service;

import com.geo.news.exception.HttpRequestErrorHandler;
import com.geo.news.model.Category;
import com.geo.news.model.CountryInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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
                item.setTitle(article.path("title").asString("—"));
                item.setDescription(article.path("description").asString("—"));
                item.setUrl(article.path("url").asString(""));
                item.setPublishedAt(article.path("publishedAt").asString(""));
                item.setSource(article.path("source").path("name").asString("—"));
                item.setImage(article.path("image").asString(""));
                newsList.add(item);
            }

        } catch (Exception e) {
            throw HttpRequestErrorHandler.toResponseStatus("GNews", e);
        }

        return newsList;
    }

    public List<CountryInfo.NewsItem> getNewsByCategory(String countryCode, String category)
    {
        List<CountryInfo.NewsItem> newsList = new ArrayList<>();
        boolean validCategory = false;
        for(Category category1 : Category.values())
        {
            if(category1.name().equalsIgnoreCase(category))
            {
                validCategory = true;
                break;
            }
        }
        if(!validCategory)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid category: " + category);
        }
        try {
            String url = "https://gnews.io/api/v4/top-headlines"
                    + "?category=" + category.toLowerCase()
                    + "&country=" + countryCode.toLowerCase()
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
                item.setTitle(article.path("title").asString("—"));
                item.setDescription(article.path("description").asString("—"));
                item.setUrl(article.path("url").asString(""));
                item.setPublishedAt(article.path("publishedAt").asString(""));
                item.setSource(article.path("source").path("name").asString("—"));
                item.setImage(article.path("image").asString(""));
                newsList.add(item);
            }

        } catch (Exception e) {
            throw HttpRequestErrorHandler.toResponseStatus("GNews", e);
        }

        return newsList;
    }

}