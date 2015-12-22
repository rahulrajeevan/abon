package ru.macrobit.abonnews.model;

public class Reg {
    private String log;
    private String email;

    public Reg(String log, String email) {
        this.log = log;
        this.email = email;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}