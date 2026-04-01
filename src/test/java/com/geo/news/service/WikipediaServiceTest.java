package com.geo.news.service;

import com.google.genai.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WikipediaServiceTest {

    private ObjectProvider<Client> clientProvider;
    private WikipediaService wikipediaService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        @SuppressWarnings("unchecked")
        ObjectProvider<Client> providerMock = Mockito.mock(ObjectProvider.class);
        clientProvider = providerMock;
        wikipediaService = new WikipediaService(clientProvider);

        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(wikipediaService, "restTemplate");
        assertNotNull(restTemplate);
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void getSummary_returnsExtractForMappedCountryCode() {
        mockServer.expect(requestTo("https://en.wikipedia.org/api/rest_v1/page/summary/Romania"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"extract\":\"Short history\"}", MediaType.APPLICATION_JSON));

        String summary = wikipediaService.getSummary("RO");

        assertEquals("Short history", summary);
        mockServer.verify();
    }

    @Test
    void getSummary_usesCountryCodeWhenMappingDoesNotExist() {
        mockServer.expect(requestTo("https://en.wikipedia.org/api/rest_v1/page/summary/ZZ"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"extract\":\"Unknown country summary\"}", MediaType.APPLICATION_JSON));

        String summary = wikipediaService.getSummary("ZZ");

        assertEquals("Unknown country summary", summary);
        mockServer.verify();
    }

    @Test
    void getSummary_returnsFallbackWhenExtractMissing() {
        mockServer.expect(requestTo("https://en.wikipedia.org/api/rest_v1/page/summary/France"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        String summary = wikipediaService.getSummary("FR");

        assertEquals("Nu s-a găsit rezumat.", summary);
        mockServer.verify();
    }

    @Test
    void getSummary_translatesWikipediaServerFailureToBadGateway() {
        mockServer.expect(requestTo("https://en.wikipedia.org/api/rest_v1/page/summary/Germany"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> wikipediaService.getSummary("DE")
        );

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Wikipedia request failed with status 500"));
        mockServer.verify();
    }

    @Test
    void getAiSummary_returnsServiceUnavailableWhenGeminiClientMissing() {
        Mockito.when(clientProvider.getIfAvailable()).thenReturn(null);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> wikipediaService.getAiSummary("RO")
        );

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
        assertEquals("Gemini client is not configured. Set GEMINI_KEY.", exception.getReason());
    }
}
