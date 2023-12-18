package org.example;

public enum ServerResponse {
    SUCCESS ("SUCCESS"),
    QUERY_NOT_FOUND ("QUERY_NOT_FOUND"),
    INVALID_PARAMS ("INVALID_QUERY");

    private String status;

    ServerResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
