package com.geo.news.controller;

import com.geo.news.model.CountryInfo;
import com.geo.news.service.CountryService;
import com.geo.news.service.NewsService;
import com.geo.news.service.WikipediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;
    private final NewsService newsService;
    private final WikipediaService wikipediaService;

    public CountryController(CountryService countryService, NewsService newsService, WikipediaService wikipediaService) {
        this.countryService = countryService;
        this.newsService = newsService;
        this.wikipediaService = wikipediaService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<CountryInfo> getCountryInfo(@PathVariable String code) {
        CountryInfo info = countryService.getCountryInfo(code);
        return ResponseEntity.ok(info);
    }

    @GetMapping({"/{code}/{category}"})
    public ResponseEntity<List<CountryInfo.NewsItem>> getNewsByCategory(@PathVariable String code, @PathVariable String category) {
        List<CountryInfo.NewsItem> newsItem = newsService.getNewsByCategory(code, category);
        return ResponseEntity.ok(newsItem);
    }

    @GetMapping("/history/{code}")
    public ResponseEntity<String> getAiSummary(@PathVariable String code) {
        return wikipediaService.getAiSummary(code);
    }

}
