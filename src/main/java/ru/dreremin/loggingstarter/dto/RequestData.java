package ru.dreremin.loggingstarter.dto;

public class RequestData {

    private String direction;
    private String method;
    private String uri;
    private String headers;
    private String body;

    public RequestData() {
        this.direction = "";
        this.method = "";
        this.uri = "";
        this.headers = "";
        this.body = "";
    }

    public static Builder builder() {
        return new Builder(new RequestData());
    }

    public static class Builder {

        private final RequestData requestData;

        private Builder(RequestData requestData) {
            this.requestData = requestData;
        }

        public Builder method(String method) {
            requestData.setMethod(method);
            return this;
        }

        public Builder uri(String uri) {
            requestData.setUri(uri);
            return this;
        }

        public Builder headers(String headers) {
            requestData.setHeaders(headers);
            return this;
        }

        public Builder body(String body) {
            requestData.setBody(body);
            return this;
        }

        public Builder direction(RequestDirection direction) {
            requestData.setDirection(direction);
            return this;
        }

        public RequestData build() {
            return requestData;
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDirection(RequestDirection direction) {
        this.direction = direction.name();
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s".formatted(direction, method, uri, headers, body);
    }
}
