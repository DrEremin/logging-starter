package ru.dreremin.loggingstarter.property;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConditionalOnProperty(prefix = "logging.exclusion", value = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "logging.exclusion")
public class LoggingStarterProperties {

    private List<String> headers = new ArrayList<>();
    private List<String> bodyPaths = new ArrayList<>();
    private List<String> uriPaths = new ArrayList<>();

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
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
