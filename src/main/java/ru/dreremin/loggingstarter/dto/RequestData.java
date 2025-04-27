package ru.dreremin.loggingstarter.dto;

public class RequestData {

    private final String direction;
    private final String method;
    private final String uri;
    private final String headers;
    private final String body;

    public RequestData(String direction, String method, String uri, String headers, String body) {
        this.direction = direction;
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s".formatted(direction, method, uri, headers, body);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String direction = "";
        private String method = "";
        private String uri = "";
        private String headers = "";
        private String body = "";

        public Builder direction(String direction) {
            this.direction = direction;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public RequestData build() {
            return new RequestData(direction, method, uri, headers, body);
        }
    }
}
