package ru.macrobit.abonnews.model;

/**
 * Created by Ghost Surfer on 05.07.2015.
 */
public class ErrorJson {
    String code;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
