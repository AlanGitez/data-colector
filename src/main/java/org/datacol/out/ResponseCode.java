package org.datacol.out;

public enum ResponseCode {
    SUCCESS("Ok", 200),
    FAILURE("Fail", 400),
    UNAUTHORIZED("Unauthorized", 401),
    NOT_FOUND("Resource not found", 404);


    private String text;
    private int statusCode;

    ResponseCode(String text, int statusCode) {
        this.text = text;
        this.statusCode = statusCode;
    }

    public String getText() {
        return this.text;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
