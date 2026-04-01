package com.geo.news.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

public final class HttpRequestErrorHandler {

    private HttpRequestErrorHandler() {
    }

    public static ResponseStatusException toResponseStatus(String provider, Exception exception) {
        if (exception instanceof ResponseStatusException responseStatusException) {
            return responseStatusException;
        }

        if (exception instanceof ResourceAccessException) {
            return new ResponseStatusException(
                    HttpStatus.GATEWAY_TIMEOUT,
                    provider + " request timed out",
                    exception
            );
        }

        if (exception instanceof HttpStatusCodeException httpStatusCodeException) {
            return new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    provider + " request failed with status " + httpStatusCodeException.getStatusCode().value(),
                    exception
            );
        }

        return new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                provider + " request failed",
                exception
        );
    }
}

