package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewsServer {
    static ServerSocket server = null;
    static Socket socket = null;
    static BufferedReader in = null;
    static PrintWriter out = null;
    static NewsDAO newsDAO;
    public static String generateLineWithSeparator(String... args) {
        StringBuilder line = new StringBuilder();
        for (String arg : args) {
            line.append(arg).append("%");
        }
        return line.toString();
    }

    public static void sendResponse(String... args) {
        out.println(generateLineWithSeparator(args));
    }

    public static boolean paramsAreInvalid(int expected, int actual) {
        if (expected != actual) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(12345);
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            newsDAO = new NewsDAO();

            while (processQuery());
        } catch (IOException | SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (newsDAO != null) {
                newsDAO.stop();
            }
        }
    }
    public static void addNewsCategory(String[] queryParts) {
        try {
            System.out.println("SERVER: addNewsCategory");
            if (paramsAreInvalid(3, queryParts.length)) return;
            int categoryId = Integer.parseInt(queryParts[1]);
            String categoryName = queryParts[2];
            newsDAO.addNewsCategory(categoryId, categoryName);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "News category added successfully");
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }


    public static void deleteNewsCategory(String[] queryParts) {
        try {
            System.out.println("SERVER: deleteNewsCategory");
            if (paramsAreInvalid(2, queryParts.length)) return;
            int categoryId = Integer.parseInt(queryParts[1]);
            newsDAO.deleteNewsCategoryById(categoryId);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "News category deleted successfully");
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }

    public static void addNewsToCategory(String[] queryParts) {
        try {
            System.out.println("SERVER: addNewsToCategory");
            if (paramsAreInvalid(4, queryParts.length)) return;
            int newsId = Integer.parseInt(queryParts[1]);
            String newsTitle = queryParts[2];
            int newsCategoryId = Integer.parseInt(queryParts[3]);
            newsDAO.addNews(newsCategoryId, newsId, newsTitle);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "News added successfully");
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }


    public static void deleteNews(String[] queryParts) {
        try {
            System.out.println("SERVER: deleteNews");
            if (paramsAreInvalid(3, queryParts.length)) return;
            int newsId = Integer.parseInt(queryParts[2]);
            newsDAO.deleteNews(newsId);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "News deleted successfully");
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }

    public static void editNews(String[] queryParts) {
        try {
            System.out.println("SERVER: editNews");
            if (paramsAreInvalid(5, queryParts.length)) return;
            int categoryId = Integer.parseInt(queryParts[1]);
            int newsId = Integer.parseInt(queryParts[2]);
            String newsTitle = queryParts[3];
            newsDAO.updateNews(newsId, categoryId, newsTitle);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "News updated successfully");
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }

    public static void countNewsInCategory(String[] queryParts) {
        try {
            System.out.println("SERVER: countNewsInCategory");
            if (paramsAreInvalid(2, queryParts.length)) return;
            int categoryId = Integer.parseInt(queryParts[1]);
            int count = newsDAO.countNewsInCategory(categoryId);
            sendResponse(ServerResponse.SUCCESS.getStatus(), "Count: " + String.valueOf(count));
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }

    public static void searchNewsByTitle(String[] queryParts) {
        try {
            System.out.println("SERVER: searchNewsByTitle");
            if (paramsAreInvalid(2, queryParts.length)) return;
            String newsTitle = queryParts[1];
            News news = newsDAO.searchNewsByText(newsTitle);
            if (news == null) {
                sendResponse(ServerResponse.SUCCESS.getStatus(), "News not found");
            } else {
                sendResponse(ServerResponse.SUCCESS.getStatus(), "News found", news.toString());
            }
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }


    public static void getAllNewsInCategory(String[] queryParts) {
        try {
            System.out.println("SERVER: getAllNewsInCategory");
            if (paramsAreInvalid(2, queryParts.length)) return;
            int categoryId = Integer.parseInt(queryParts[1]);
            ArrayList<News> newsList = newsDAO.getAllNewsInCategory(categoryId);
            if (newsList.isEmpty()) {
                sendResponse(ServerResponse.SUCCESS.getStatus(), "Category not found");
            } else {
                for (News news : newsList) {
                    sendResponse(news.toString());
                }
            }
            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }

    public static void getAllCategories() {
        try {
            System.out.println("SERVER: Getting all categories");
            ArrayList<NewsCategory> categories = newsDAO.getAllCategories();

            if (categories.isEmpty()) {
                sendResponse(ServerResponse.SUCCESS.getStatus(), "No categories found");
            } else {
                for (NewsCategory category : categories) {
                    sendResponse(ServerResponse.SUCCESS.getStatus(), category.toString());
                }
            }

            sendResponse("END_OF_RESPONSE");
        } catch (Exception e) {
            sendResponse(ServerResponse.INVALID_PARAMS.getStatus());
        }
    }





    public static boolean processQuery() {
        try {
            String query = in.readLine();
            String[] queryParts = parseQuery(query);
            String command = queryParts[0];
            switch (command) {
                case "1" -> addNewsCategory(queryParts);
                case "2" -> deleteNewsCategory(queryParts);
                case "3" -> addNewsToCategory(queryParts);
                case "4" -> deleteNews(queryParts);
                case "5" -> editNews(queryParts);
                case "6" -> countNewsInCategory(queryParts);
                case "7" -> searchNewsByTitle(queryParts);
                case "8" -> getAllNewsInCategory(queryParts);
                case "9" -> getAllCategories();
                case "10" -> {
                    System.out.println("SERVER: Exit");
                    socket.close();
                    server.close();
                    return false;
                }
                default -> sendResponse(ServerResponse.QUERY_NOT_FOUND.getStatus());
            }
            return true;
        } catch (Exception e) {
            System.out.println("SERVER: Error: " + e.getMessage());
            return false;
        }
    }

    public static String[] parseQuery(String query) {
        return query.split("%");
    }
}
