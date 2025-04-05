package ru.dreremin.loggingstarter.service;

import feign.Request;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.dreremin.loggingstarter.dto.RequestData;
import ru.dreremin.loggingstarter.dto.ResponseData;
import ru.dreremin.loggingstarter.dto.RequestDirection;
import ru.dreremin.loggingstarter.masking.JsonBodyPropertiesMasker;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.util.RequestDataFormatter;

import java.nio.charset.StandardCharsets;

public class LoggingService {

    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    @Autowired
    private LoggingStarterProperties properties;

    @Autowired
    private JsonBodyPropertiesMasker masker;

    public void logRequest(HttpServletRequest request) {
        RequestData requestData = RequestData.builder()
                .direction(RequestDirection.IN)
                .method(request.getMethod())
                .uri(RequestDataFormatter.formatRequestUriWithQueryParams(request))
                .headers(RequestDataFormatter.formatRequestHeaders(request, properties.getHeaders()))
                .build();

        log.info("Запрос: {}", requestData);
    }

    public void logRequestBody(HttpServletRequest request, Object body) {
        RequestData requestData = RequestData.builder()
                .direction(RequestDirection.IN)
                .method(request.getMethod())
                .uri(RequestDataFormatter.formatRequestUriWithQueryParams(request))
                .headers(RequestDataFormatter.formatRequestHeaders(request, properties.getHeaders()))
                .body(prepareBody(body))
                .build();

        log.info("Запрос: {}", requestData);
    }

    public void logFeignRequest(Request request) {
        byte[] bodyData = request.body();
        String requestBody = bodyData != null ? new String(bodyData, StandardCharsets.UTF_8) : "";
        RequestData requestData = RequestData.builder()
                .direction(RequestDirection.OUT)
                .method(request.httpMethod().name())
                .uri(request.url())
                .headers(RequestDataFormatter.formatFeignRequestHeaders(request, properties.getHeaders()))
                .body(prepareBody(requestBody))
                .build();

        log.info("Запрос: {}", requestData);
    }

    public void logResponse(HttpServletRequest request, ContentCachingResponseWrapper responseWrapper) {
        String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        ResponseData responseData = ResponseData.builder()
                .direction(RequestDirection.IN)
                .method(request.getMethod())
                .uri(RequestDataFormatter.formatRequestUriWithQueryParams(request))
                .status(responseWrapper.getStatus())
                .body(prepareBody(responseBody))
                .build();

        log.info("Ответ: {}", responseData);
    }

    public void logFeignResponse(Response response, String responseBody) {
        ResponseData responseData = ResponseData.builder()
                .direction(RequestDirection.OUT)
                .method(response.request().httpMethod().name())
                .uri(response.request().url())
                .status(response.status())
                .body(prepareBody(responseBody))
                .build();

        log.info("Ответ: {}", responseData);
    }

    private String prepareBody(Object body) {
        return masker.maskProperties(body, properties.getBodyPaths())
                .map(s -> "body=" + s)
                .orElse("");
    }
}
