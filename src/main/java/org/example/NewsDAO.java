package org.example;


import java.sql.*;
import java.util.ArrayList;

public class NewsDAO {
    private final String DB_URL = "jdbc:mysql://localhost:3306/news_agency";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "123456789";

    Statement statement = null;
    Connection connection = null;

    public NewsDAO() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
       // Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();
    }

    public void addNews(int categoryId, int newsId, String text) {
        try {
            String query = "INSERT INTO news_agency.news (ID_CO, ID_NE, NAME) VALUES (" + categoryId + ", '" + newsId + "', '" + text + "')";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewsCategory(int categoryId, String categoryName) {
        try {
            String query = "INSERT INTO news_agency.news_categories (ID_CO, NAME) VALUES (" + categoryId + ", '" + categoryName + "')";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteNews(int newsId) {
        try {
            String query = "DELETE FROM news_agency.news WHERE ID_NE = " + newsId;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteNewsCategoryById(int categoryId) {
        try {
            String query = "DELETE FROM news_agency.news_categories WHERE ID_CO  = " + categoryId;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNews(int newsId, int categoryId, String text) {
        try {
            String query = "UPDATE news_agency.news SET ID_CO = " + categoryId + ", NAME = '" + text + "' WHERE ID_NE = " + newsId;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countNewsInCategory(int categoryId) {
        try {
            String query = "SELECT COUNT(*) FROM news_agency.news WHERE ID_CO = " + categoryId;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public News searchNewsByText(String newsTitle) {
        try {
            String query = "SELECT * FROM news_agency.news WHERE NAME = '" + newsTitle + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                News news = new News(resultSet.getInt("ID_NE"), resultSet.getString("NAME"));
                resultSet.close();
                return news;
            } else {
                resultSet.close();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<News> getAllNewsInCategory(int categoryId) {
        try {
            String query = "SELECT * FROM news_agency.news WHERE ID_CO = " + categoryId;
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<News> newsList = new ArrayList<>();
            while (resultSet.next()) {
                newsList.add(new News(resultSet.getInt("ID_NE"), resultSet.getString("NAME")));
            }
            resultSet.close();
            return newsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<NewsCategory> getAllCategories() {
        try {
            String query = "SELECT * FROM news_agency.news_categories";
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<NewsCategory> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(new NewsCategory(resultSet.getInt("ID_CO"), resultSet.getString("NAME")));
            }
            resultSet.close();
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
