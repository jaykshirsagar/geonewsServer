package com.geo.news.service;

import com.geo.news.model.Category;
import com.geo.news.model.CountryInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        newsService = new NewsService();

        Field restTemplateField = NewsService.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(newsService, restTemplate);

        Field objectMapperField = NewsService.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(newsService, objectMapper);

        Field apiKeyField = NewsService.class.getDeclaredField("apiKey");
        apiKeyField.setAccessible(true);
        apiKeyField.set(newsService, "test-key");
    }

    @Test
    void testGetNews_success() throws Exception {
        String body = "json";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        JsonNode root = mock(JsonNode.class);
        JsonNode articles = mock(JsonNode.class);
        JsonNode article = mock(JsonNode.class);

        JsonNode sourceNode = mock(JsonNode.class);
        JsonNode sourceNameNode = mock(JsonNode.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readTree(body)).thenReturn(root);
        when(root.path("articles")).thenReturn(articles);
        when(articles.iterator()).thenReturn(List.of(article).iterator());

        when(article.path("title")).thenReturn(mock(JsonNode.class));
        when(article.path("title").asString("—")).thenReturn("title");

        when(article.path("description")).thenReturn(mock(JsonNode.class));
        when(article.path("description").asString("—")).thenReturn("desc");

        when(article.path("url")).thenReturn(mock(JsonNode.class));
        when(article.path("url").asString("")).thenReturn("url");

        when(article.path("publishedAt")).thenReturn(mock(JsonNode.class));
        when(article.path("publishedAt").asString("")).thenReturn("date");

        when(article.path("image")).thenReturn(mock(JsonNode.class));
        when(article.path("image").asString("")).thenReturn("img");

        when(article.path("source")).thenReturn(sourceNode);
        when(sourceNode.path("name")).thenReturn(sourceNameNode);
        when(sourceNameNode.asString("—")).thenReturn("source");

        List<CountryInfo.NewsItem> result = newsService.getNews("RO");

        assertEquals(1, result.size());
        assertEquals("title", result.get(0).getTitle());
    }

    @Test
    void testGetNews_exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> newsService.getNews("RO"));
    }

    @Test
    void testGetNewsByCategory_success() throws Exception {
        String body = "json";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        JsonNode root = mock(JsonNode.class);
        JsonNode articles = mock(JsonNode.class);
        JsonNode article = mock(JsonNode.class);

        JsonNode sourceNode = mock(JsonNode.class);
        JsonNode sourceNameNode = mock(JsonNode.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(responseEntity);

        when(objectMapper.readTree(body)).thenReturn(root);
        when(root.path("articles")).thenReturn(articles);
        when(articles.iterator()).thenReturn(List.of(article).iterator());

        when(article.path("title")).thenReturn(mock(JsonNode.class));
        when(article.path("title").asString("—")).thenReturn("title");

        when(article.path("description")).thenReturn(mock(JsonNode.class));
        when(article.path("description").asString("—")).thenReturn("desc");

        when(article.path("url")).thenReturn(mock(JsonNode.class));
        when(article.path("url").asString("")).thenReturn("url");

        when(article.path("publishedAt")).thenReturn(mock(JsonNode.class));
        when(article.path("publishedAt").asString("")).thenReturn("date");

        when(article.path("image")).thenReturn(mock(JsonNode.class));
        when(article.path("image").asString("")).thenReturn("img");

        when(article.path("source")).thenReturn(sourceNode);
        when(sourceNode.path("name")).thenReturn(sourceNameNode);
        when(sourceNameNode.asString("—")).thenReturn("source");

        List<CountryInfo.NewsItem> result =
                newsService.getNewsByCategory("RO", Category.GENERAL.name());

        assertEquals(1, result.size());
    }

    @Test
    void testGetNewsByCategory_invalidCategory() {
        assertThrows(ResponseStatusException.class,
                () -> newsService.getNewsByCategory("RO", "invalid"));
    }

    @Test
    void testGetNewsByCategory_exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,
                () -> newsService.getNewsByCategory("RO", Category.GENERAL.name()));
    }
}