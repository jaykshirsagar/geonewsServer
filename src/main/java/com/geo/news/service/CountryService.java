package com.geo.news.service;

import com.geo.news.model.CountryInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CountryService {

    private final NewsService newsService;
    private final WikipediaService wikipediaService;
    private final UnsplashService unsplashService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CountryService(NewsService newsService, WikipediaService wikipediaService,
                          UnsplashService unsplashService) {
        this.newsService = newsService;
        this.wikipediaService = wikipediaService;
        this.unsplashService = unsplashService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public CountryInfo getCountryInfo(String countryCode) {
        CountryInfo info = new CountryInfo();
        info.setCode(countryCode);

        try {
            String url = "https://restcountries.com/v3.1/alpha/" + countryCode;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response).get(0);

            // Nume
            info.setName(root.path("name").path("common").asText("—"));

            // Capitală
            JsonNode capitals = root.path("capital");
            info.setCapital(capitals.isArray() && capitals.size() > 0
                    ? capitals.get(0).asText("—") : "—");

            // Populație
            long pop = root.path("population").asLong(0);
            info.setPopulation(String.format("%,d", pop));

            // Limbă
            JsonNode languages = root.path("languages");
            String language = "—";
            if (languages.isObject()) {
                var iterator = languages.properties();
                if (iterator.iterator().hasNext()) {
                    language = iterator.iterator().next().getValue().asText("—");
                }
            }
            info.setLanguage(language);

            // Monedă
            JsonNode currencies = root.path("currencies");
            String currency = "—";
            if (currencies.isObject()) {
                var iterator = currencies.properties();
                if (iterator.iterator().hasNext()) {
                    JsonNode currencyNode = iterator.iterator().next().getValue();
                    currency = currencyNode.path("name").asText("—")
                            + " (" + currencyNode.path("symbol").asText("?") + ")";
                }
            }
            info.setCurrency(currency);

            // Continent
            JsonNode continents = root.path("continents");
            info.setContinent(continents.isArray() && continents.size() > 0
                    ? continents.get(0).asText("—") : "—");

            // Suprafață
            double area = root.path("area").asDouble(0);
            info.setArea(String.format("%,.0f km²", area));

            // Steag
            info.setFlagUrl(root.path("flags").path("png").asText(""));

        } catch (Exception e) {
            info.setName("—");
            info.setCapital("—");
            info.setPopulation("—");
            info.setLanguage("—");
            info.setCurrency("—");
            info.setContinent("—");
            info.setArea("—");
            info.setFlagUrl("");
        }

        String countryName = info.getName();
        if (countryName == null || countryName.equals("—")) {
            countryName = countryCode;
        }
        info.setHistorySummary(wikipediaService.getSummary(countryCode));
        info.setPhotos(unsplashService.getPhotos(countryName));
        info.setNews(newsService.getNews(countryCode));

        return info;
    }
}