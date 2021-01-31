package org.techtown.bangboard2;

public class Board {
    String title;
    String content;
    String date;
    String uid;

    public Board (String title, String content, String date, String uid) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String user) {
        this.uid = uid;
    }
}
