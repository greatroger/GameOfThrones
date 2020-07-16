package com.example.gameofthrones;

public class Characters {
    public int pageid;
    public String title;
    public Characters() {

    }
    public Characters(int pageid, String tilte) {
        this.pageid = pageid;
        this.title = tilte;
    }
    public int getId() {
        return pageid;
    }

    public void setId(int pageid) {
        this.pageid = pageid;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
