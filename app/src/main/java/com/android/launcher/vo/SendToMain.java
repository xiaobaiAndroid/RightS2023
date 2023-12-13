package com.android.launcher.vo;

public class SendToMain {

    int id ;

    String c;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "SendToMain{" +
                "id=" + id +
                ", content='" + c + '\'' +
                '}';
    }
}
