package ru.dreremin.loggingstarter.property;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties(prefix = "logging.exclusion")
public class LoggingStarterProperties {

    private Set<String> headers;
    private List<String> bodyPaths;
    private List<String> uriPaths;

    public LoggingStarterProperties() {
        this.bodyPaths = new ArrayList<>();
        this.uriPaths = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        Set<String> headersLowerCase = new HashSet<>();

        if (headers != null) {
            for (String header : headers) {
                headersLowerCase.add(header.toLowerCase());
            }
        }

        headers = headersLowerCase;
    }

    public Set<String> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<String> headers) {
        this.headers = headers;
    }

    public List<String> getBodyPaths() {
        return bodyPaths;
    }

    public void setBodyPaths(List<String> bodyPaths) {
        this.bodyPaths = bodyPaths;
    }

    public List<String> getUriPaths() {
        return uriPaths;
    }

    public void setUriPaths(List<String> uriPaths) {
        this.uriPaths = uriPaths;
    }
}
