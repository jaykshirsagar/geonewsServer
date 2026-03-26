package com.geo.news.model;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountryInfo {
    // Info de bază
    private String name;
    private String code;
    private String capital;
    private String population;
    private String language;
    private String currency;
    private String continent;
    private String area;
    private String flagUrl;

    // Wikipedia
    private String historySummary;

    // Unsplash
    private List<String> photos;

    // NewsAPI
    private List<NewsItem> news;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class NewsItem {
        private String title;
        private String description;
        private String url;
        private String publishedAt;
        private String source;
        private String image;
    }
}
