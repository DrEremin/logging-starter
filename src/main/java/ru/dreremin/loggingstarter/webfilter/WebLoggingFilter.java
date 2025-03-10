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
import ru.dreremin.loggingstarter.exclusion.EndpointExclusionFlagQualifier;
import ru.dreremin.loggingstarter.property.AppProperties;
import ru.dreremin.loggingstarter.util.RequestDataFormatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WebLoggingFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingFilter.class);

    @Autowired
    private EndpointExclusionFlagQualifier endpointExclusionFlagQualifier;

    @Autowired
    private AppProperties appProperties;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        String method = request.getMethod();
        String requestURI = RequestDataFormatter.formatRequestUriWithQueryParams(request);
        String headers = RequestDataFormatter.formatRequestHeaders(request, appProperties.getHeaders());
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            endpointExclusionFlagQualifier.setExclusionFlag(request.getRequestURI(), appProperties.getUriPaths());
            super.doFilter(request, responseWrapper, chain);
            if (endpointExclusionFlagQualifier.isNotExclusion()) {
                if (method.equals("GET") || method.equals("DELETE")) {
                    log.info("Запрос: {} {} {}", method, requestURI, headers);
                }
                String responseBody = "body=" + new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                log.info("Ответ: {} {} {} {}", method, requestURI, response.getStatus(), responseBody);
            }
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }
}