package com.example.myapplication;

public class BoardInfo3 {
    String userID;
    String title;
    String content;
    String date;

    public BoardInfo3(String userID, String title, String content, String date) {
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

}
