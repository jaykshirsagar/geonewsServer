package com.geo.news.service;

import com.geo.news.model.CountryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @Mock
    private NewsService newsService;

    @Mock
    private WikipediaService wikipediaService;

    @Mock
    private UnsplashService unsplashService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        countryService = new CountryService(newsService, wikipediaService, unsplashService);

        Field restTemplateField = CountryService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(countryService, restTemplate);

        Field objectMapperField = CountryService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(countryService, objectMapper);
    }

    @Test
    void testGetCountryInfo_success() throws Exception {
        String code = "RO";
        String mockResponse = "json";

        JsonNode rootArray = mock(JsonNode.class);
        JsonNode root = mock(JsonNode.class);

        JsonNode nameNode = mock(JsonNode.class);
        JsonNode commonNode = mock(JsonNode.class);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(objectMapper.readTree(mockResponse)).thenReturn(rootArray);
        when(rootArray.get(0)).thenReturn(root);

        when(root.path("name")).thenReturn(nameNode);
        when(nameNode.path("common")).thenReturn(commonNode);
        when(commonNode.asString("—")).thenReturn("Romania");

        JsonNode capitalArray = mock(JsonNode.class);
        JsonNode capitalValue = mock(JsonNode.class);
        when(root.path("capital")).thenReturn(capitalArray);
        when(capitalArray.isArray()).thenReturn(true);
        when(capitalArray.size()).thenReturn(1);
        when(capitalArray.get(0)).thenReturn(capitalValue);
        when(capitalValue.asString("—")).thenReturn("Bucharest");

        JsonNode populationNode = mock(JsonNode.class);
        when(root.path("population")).thenReturn(populationNode);
        when(populationNode.asLong(0)).thenReturn(19000000L);

        JsonNode languages = mock(JsonNode.class);
        when(root.path("languages")).thenReturn(languages);
        when(languages.isObject()).thenReturn(false);

        JsonNode currencies = mock(JsonNode.class);
        when(root.path("currencies")).thenReturn(currencies);
        when(currencies.isObject()).thenReturn(false);

        JsonNode continents = mock(JsonNode.class);
        JsonNode continentValue = mock(JsonNode.class);
        when(root.path("continents")).thenReturn(continents);
        when(continents.isArray()).thenReturn(true);
        when(continents.size()).thenReturn(1);
        when(continents.get(0)).thenReturn(continentValue);
        when(continentValue.asString("—")).thenReturn("Europe");

        JsonNode areaNode = mock(JsonNode.class);
        when(root.path("area")).thenReturn(areaNode);
        when(areaNode.asDouble(0)).thenReturn(238397.0);

        JsonNode flagsNode = mock(JsonNode.class);
        JsonNode pngNode = mock(JsonNode.class);
        when(root.path("flags")).thenReturn(flagsNode);
        when(flagsNode.path("png")).thenReturn(pngNode);
        when(pngNode.asString("")).thenReturn("flag.png");

        when(wikipediaService.getSummary(code)).thenReturn("history");
        when(unsplashService.getPhotos("Romania")).thenReturn(List.of("photo1"));
        when(newsService.getNews(code)).thenReturn(List.of());

        CountryInfo result = countryService.getCountryInfo(code);

        assertEquals("RO", result.getCode());
        assertEquals("Romania", result.getName());
        assertEquals("Bucharest", result.getCapital());
        assertEquals("19,000,000", result.getPopulation());
        assertEquals("Europe", result.getContinent());
        assertEquals("238,397 km²", result.getArea());
        assertEquals("flag.png", result.getFlagUrl());
        assertEquals("history", result.getHistorySummary());

        verify(newsService).getNews(code);
        verify(wikipediaService).getSummary(code);
        verify(unsplashService).getPhotos("Romania");
    }

    @Test
    void testGetCountryInfo_exception() {
        String code = "RO";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("error"));

        assertThrows(RuntimeException.class, () -> countryService.getCountryInfo(code));
    }
}