package ru.dreremin.loggingstarter.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@ConfigurationProperties(prefix = "logging.exclusion")
public class LoggingStarterProperties {

    private Set<String> headers;
    private List<String> bodyPaths;
    private List<String> uriPaths;

    public LoggingStarterProperties() {
        this.headers = new HashSet<>();
        this.bodyPaths = new ArrayList<>();
        this.uriPaths = new ArrayList<>();
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
