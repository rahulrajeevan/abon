package ru.macrobit.abonnews.model;

/**
 * Created by Comp on 14.07.2015.
 */
public class PushReg {
    private String id;
    private String pushid;
    private String type;

    public PushReg(String pushid) {
        this.pushid = pushid;
        this.type = "android";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
