package org.example;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsCategory implements Serializable {
    public int categoryId;
    public String categoryName;
    public ArrayList<News> newsList;

    public NewsCategory(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.newsList = new ArrayList<>();
    }

    public void addNews(News news) {
        this.newsList.add(news);
    }

    public ArrayList<News> getNewsList() {
        return this.newsList;
    }

    public News getNews(int newsId) {
        for (News news : newsList) {
            if (news.getNewsId() == newsId) {
                return news;
            }
        }
        return null;
    }

    public void deleteNews(int newsId) {
        News newsToRemove = null;
        for (News news : newsList) {
            if (news.getNewsId() == newsId) {
                newsToRemove = news;
                break;
            }
        }
        if (newsToRemove != null) {
            newsList.remove(newsToRemove);
        }
    }

    public void updateNews(int newsId, News updatedNews) {
        for (int i = 0; i < newsList.size(); i++) {
            if (newsList.get(i).getNewsId() == newsId) {
                newsList.set(i, updatedNews);
                break;
            }
        }
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category ID: " + categoryId + ", Category Name: " + categoryName;
    }
}

