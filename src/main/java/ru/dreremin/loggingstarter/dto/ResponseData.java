package ru.dreremin.loggingstarter.dto;

public class ResponseData {

    private String direction;
    private String method;
    private String uri;
    private String status;
    private String body;

    public ResponseData() {
        this.method = "";
        this.uri = "";
        this.status = "";
        this.body = "";
        this.direction = "";
    }

    public static Builder builder() {
        return new Builder(new ResponseData());
    }

    public static class Builder {

        private final ResponseData responseData;

        private Builder(ResponseData responseData) {
            this.responseData = responseData;
        }

        public Builder method(String method) {
            responseData.setMethod(method);
            return this;
        }

        public Builder uri(String uri) {
            responseData.setUri(uri);
            return this;
        }

        public Builder status(int status) {
            responseData.setStatus(status);
            return this;
        }

        public Builder body(String body) {
            responseData.setBody(body);
            return this;
        }

        public Builder direction(RequestDirection direction) {
            responseData.setDirection(direction);
            return this;
        }

        public ResponseData build() {
            return responseData;
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setStatus(int status) {
        this.status = String.valueOf(status);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDirection(RequestDirection direction) {
        this.direction = direction.name();
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s".formatted(direction, method, uri, status, body);
    }
}
