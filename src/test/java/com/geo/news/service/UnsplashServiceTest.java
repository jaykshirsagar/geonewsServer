package com.geo.news.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnsplashServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UnsplashService unsplashService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        unsplashService = new UnsplashService();

        Field restTemplateField = UnsplashService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(unsplashService, restTemplate);

        Field objectMapperField = UnsplashService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(unsplashService, objectMapper);

        Field apiKeyField = UnsplashService.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(unsplashService, "test-key");
    }

    @Test
    void testGetPhotos_success() throws Exception {
        String body = "json";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        JsonNode root = mock(JsonNode.class);
        JsonNode results = mock(JsonNode.class);
        JsonNode photo = mock(JsonNode.class);
        JsonNode urls = mock(JsonNode.class);
        JsonNode regular = mock(JsonNode.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readTree(body)).thenReturn(root);
        when(root.path("results")).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(photo).iterator());

        when(photo.path("urls")).thenReturn(urls);
        when(urls.path("regular")).thenReturn(regular);
        when(regular.asString("")).thenReturn("image-url");

        List<String> result = unsplashService.getPhotos("Romania");

        assertEquals(1, result.size());
        assertEquals("image-url", result.get(0));
    }

    @Test
    void testGetPhotos_emptyUrls() throws Exception {
        String body = "json";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        JsonNode root = mock(JsonNode.class);
        JsonNode results = mock(JsonNode.class);
        JsonNode photo = mock(JsonNode.class);
        JsonNode urls = mock(JsonNode.class);
        JsonNode regular = mock(JsonNode.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readTree(body)).thenReturn(root);
        when(root.path("results")).thenReturn(results);
        when(results.iterator()).thenReturn(List.of(photo).iterator());

        when(photo.path("urls")).thenReturn(urls);
        when(urls.path("regular")).thenReturn(regular);
        when(regular.asString("")).thenReturn("");

        List<String> result = unsplashService.getPhotos("Romania");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPhotos_exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> unsplashService.getPhotos("Romania"));
    }
}