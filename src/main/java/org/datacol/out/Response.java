package org.datacol.out;


public class Response {

    private Object entity;

    private Integer statusCode;

    private String msg;

    private boolean error;

    private String detailedError;


    public Response(Object entity, ResponseCode code) {
        this.entity = entity;
        this.statusCode = code.getStatusCode();
        this.msg = code.getText();
        this.error = false;
        this.detailedError = null;
    }

    public Response(ResponseCode code, String detailedError) {
        this.statusCode = code.getStatusCode();
        this.msg = code.getText();
        this.error = true;
        this.detailedError = detailedError;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getDetailedError() {
        return detailedError;
    }

    public void setDetailedError(String detailedError) {
        this.detailedError = detailedError;
    }


}
