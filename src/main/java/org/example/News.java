package org.example;


import java.io.Serializable;

public class News implements Serializable {
    private int newsId;
    private String title;
    private String text;

    public News(int newsId, String title) {
        this.newsId = newsId;
        this.title = title;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "News ID: " + newsId + ", Title: " + title;
    }
}

