package com.tc.execution.rest.exceptions;

import javax.ws.rs.WebApplicationException;

public class ExecutionException extends WebApplicationException {

    private String message;
    private int status;

    public ExecutionException() {
    }

    public ExecutionException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
