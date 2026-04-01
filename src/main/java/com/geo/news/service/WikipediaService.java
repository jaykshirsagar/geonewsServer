package com.geo.news.service;

import com.geo.news.exception.HttpRequestErrorHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class WikipediaService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WikipediaService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getSummary(String countryCode) {
        try {
            String countryName = getCountryName(countryCode);
            String url = "https://en.wikipedia.org/api/rest_v1/page/summary/"
                    + countryName.replace(" ", "_");

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GeoNews/1.0 (educational project; contact@geonews.com)");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("extract").asString("Nu s-a găsit rezumat.");

        } catch (Exception e) {
            throw HttpRequestErrorHandler.toResponseStatus("Wikipedia", e);
        }
    }

    private String getCountryName(String countryCode) {
        return switch (countryCode.toUpperCase()) {
            case "RO" -> "Romania";
            case "FR" -> "France";
            case "DE" -> "Germany";
            case "IT" -> "Italy";
            case "ES" -> "Spain";
            case "GB" -> "United_Kingdom";
            case "US" -> "United_States";
            case "JP" -> "Japan";
            case "CN" -> "China";
            case "BR" -> "Brazil";
            case "AU" -> "Australia";
            case "CA" -> "Canada";
            case "RU" -> "Russia";
            case "IN" -> "India";
            case "MX" -> "Mexico";
            case "AR" -> "Argentina";
            case "ZA" -> "South_Africa";
            case "NG" -> "Nigeria";
            case "EG" -> "Egypt";
            case "TR" -> "Turkey";
            case "SA" -> "Saudi_Arabia";
            case "KR" -> "South_Korea";
            case "ID" -> "Indonesia";
            case "PK" -> "Pakistan";
            case "UA" -> "Ukraine";
            case "PL" -> "Poland";
            case "NL" -> "Netherlands";
            case "BE" -> "Belgium";
            case "SE" -> "Sweden";
            case "NO" -> "Norway";
            case "CH" -> "Switzerland";
            case "AT" -> "Austria";
            case "PT" -> "Portugal";
            case "GR" -> "Greece";
            case "HU" -> "Hungary";
            case "CZ" -> "Czech_Republic";
            case "MD" -> "Moldova";
            case "BG" -> "Bulgaria";
            case "RS" -> "Serbia";
            case "HR" -> "Croatia";
            default -> countryCode;
        };
    }
}
