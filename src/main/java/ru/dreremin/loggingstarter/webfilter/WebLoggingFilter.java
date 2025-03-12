package ru.dreremin.loggingstarter.webfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.util.JsonBodyPropertiesMasker;
import ru.dreremin.loggingstarter.util.RequestDataFormatter;
import ru.dreremin.loggingstarter.util.URIPathMatcherWithPathPatterns;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WebLoggingFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingFilter.class);

    @Autowired
    private LoggingStarterProperties loggingStarterProperties;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        String method = request.getMethod();
        String requestURI = RequestDataFormatter.formatRequestUriWithQueryParams(request);
        String headers = RequestDataFormatter.formatRequestHeaders(request, loggingStarterProperties.getHeaders());
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            super.doFilter(request, responseWrapper, chain);

            if (!URIPathMatcherWithPathPatterns.matchAny(request.getRequestURI(), loggingStarterProperties.getUriPaths())) {
                if (method.equals("GET") || method.equals("DELETE")) {
                    log.info("Запрос: {} {} {}", method, requestURI, headers);
                }

                String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                log.info("Обработка тела ответа...");
                responseBody = JsonBodyPropertiesMasker.maskProperties(responseBody, loggingStarterProperties.getBodyPaths());
                log.info("Ответ: {} {} {} {}", method, requestURI, response.getStatus(), "body=" + responseBody);
            }
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }
}