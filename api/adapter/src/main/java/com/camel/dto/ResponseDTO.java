package com.camel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Error implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3925331655977411290L;
    private String code;
    private String message;
    private List<String> params;
    
    public Error() {
        super();
    }
    
    public Error(String code, String message, List<String> params) {
        super();
        this.code = code;
        this.message = message;
        this.params = params;
    }
    
    public Error(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<String> getParams() {
        return params;
    }
    
    public void setParams(List<String> params) {
        this.params = params;
    }
    
    @Override public String toString() {
        return "Error [code=" + code + ", message=" + message + ", params=" + params + "]";
    }
    
};

public class ResponseDTO <T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static enum Status {
        SUCCESS,FAILURE;
    }
    
    public ResponseDTO() {
        this.status = Status.SUCCESS;
    }
    
    private Status status;
    private List<Error> errors = new ArrayList<>();
    
    public void addError(String code, String message) {
        addError(new Error(code, message, null));
    }
    
    public void addError(String code, String message, List<String> params) {
        addError(new Error(code, message, params));
    }
    
    public void addError(String code) {
        addError(new Error(code, "", null));
    }
    
    public void addError(Error error) {
        this.status = Status.FAILURE;
        this.errors.add(error);
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public List<Error> getErrors() {
        return errors;
    }
    
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
    
    public boolean IsSuccessful() {
        if (this.getStatus() == Status.SUCCESS) {
            return true;
        }
        return false;
    }
    
    private T response;
    
    public T getResponse() {
        return response;
    }
    
    public void setResponse(T t) {
        this.response = t;
    }
    
}
