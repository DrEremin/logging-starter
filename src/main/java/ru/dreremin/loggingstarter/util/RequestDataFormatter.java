package ru.dreremin.loggingstarter.util;

import feign.Request;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.stream.Collectors;

public class RequestDataFormatter {

    public static String formatRequestUriWithQueryParams(HttpServletRequest request) {
        return request.getRequestURI() + formattedQueryString(request);
    }

    public static String formatRequestHeaders(HttpServletRequest request, List<String> excludedHeaders) {
        String inlineHeaders = Collections.list(request.getHeaderNames()).stream()
                .filter(header -> excludedHeaders.stream()
                        .noneMatch(excludedHeader -> excludedHeader.equalsIgnoreCase(header)))
                .map(it -> "%s=%s".formatted(it, request.getHeader(it)))
                .collect(Collectors.joining(","));

        return "headers={%s}".formatted(inlineHeaders);
    }

    public static String formatFeignRequestHeaders(Request request, List<String> excludedHeaders) {
        Map<String, Collection<String>> headers = request.headers();
        String inlineHeaders = headers.keySet().stream()
                .filter(header -> excludedHeaders.stream()
                        .noneMatch(excludedHeader -> excludedHeader.equalsIgnoreCase(header)))
                .map(it -> "%s=%s".formatted(it, headers.get(it)))
                .collect(Collectors.joining(","));

        return "headers={%s}".formatted(inlineHeaders);
    }

    private static String formattedQueryString(HttpServletRequest request) {
        return Optional.ofNullable(request.getQueryString())
                .map(qs -> "?" + qs)
                .orElse(Strings.EMPTY);
    }
}