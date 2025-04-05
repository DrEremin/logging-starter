package ru.dreremin.loggingstarter.webfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.service.LoggingService;
import ru.dreremin.loggingstarter.util.URIPathMatcherWithPathPatterns;

import java.io.IOException;

public class WebLoggingFilter extends HttpFilter {

    @Autowired
    private LoggingStarterProperties loggingStarterProperties;

    @Autowired
    private LoggingService loggingService;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            super.doFilter(request, responseWrapper, chain);

            if (!URIPathMatcherWithPathPatterns.matchAny(request.getRequestURI(), loggingStarterProperties.getUriPaths())) {
                if (request.getContentLength() == -1) {
                    loggingService.logRequest(request);
                }

                loggingService.logResponse(request, responseWrapper);
            }

        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }
}