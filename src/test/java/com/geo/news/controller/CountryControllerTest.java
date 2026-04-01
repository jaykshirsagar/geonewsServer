package com.geo.news.controller;

import com.geo.news.model.CountryInfo;
import com.geo.news.service.CountryService;
import com.geo.news.service.NewsService;
import com.geo.news.service.WikipediaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    @Mock
    private CountryService countryService;

    @Mock
    private NewsService newsService;

    @Mock
    private WikipediaService wikipediaService;

    @InjectMocks
    private CountryController countryController;

    public CountryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCountryInfo() {
        String code = "RO";
        CountryInfo mockInfo = mock(CountryInfo.class);

        when(countryService.getCountryInfo(code)).thenReturn(mockInfo);

        ResponseEntity<CountryInfo> response = countryController.getCountryInfo(code);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockInfo, response.getBody());
        verify(countryService, times(1)).getCountryInfo(code);
    }

    @Test
    void testGetNewsByCategory() {
        String code = "RO";
        String category = "sports";

        List<CountryInfo.NewsItem> mockList = List.of(mock(CountryInfo.NewsItem.class));

        when(newsService.getNewsByCategory(code, category)).thenReturn(mockList);

        ResponseEntity<List<CountryInfo.NewsItem>> response =
                countryController.getNewsByCategory(code, category);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockList, response.getBody());
        verify(newsService, times(1)).getNewsByCategory(code, category);
    }

    @Test
    void testGetAiSummary() {
        String code = "RO";
        ResponseEntity<String> mockResponse = ResponseEntity.ok("summary");

        when(wikipediaService.getAiSummary(code)).thenReturn(mockResponse);

        ResponseEntity<String> response = countryController.getAiSummary(code);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("summary", response.getBody());
        verify(wikipediaService, times(1)).getAiSummary(code);
    }
}