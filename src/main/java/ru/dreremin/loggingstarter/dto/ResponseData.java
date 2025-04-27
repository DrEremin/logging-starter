package ru.dreremin.loggingstarter.dto;

public class ResponseData {

    private final String direction;
    private final String method;
    private final String uri;
    private final String status;
    private final String body;

    public ResponseData(String direction, String method, String uri, String status, String body) {
        this.direction = direction;
        this.method = method;
        this.uri = uri;
        this.status = status;
        this.body = body;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s".formatted(direction, method, uri, status, body);
    }

    public static class Builder {

        private String direction = "";
        private String method = "";
        private String uri = "";
        private String status = "";
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

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public ResponseData build() {
            return new ResponseData(direction, method, uri, status, body);
        }
    }
}
