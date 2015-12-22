package ru.macrobit.abonnews.model;


public class ChangePass {
    private String pwd;

    public ChangePass(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}