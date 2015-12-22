package ru.macrobit.abonnews.model;

public class Auth {
    private String log;
    private String pwd;

    public Auth(String log, String pwd) {
        this.log = log;
        this.pwd = pwd;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}