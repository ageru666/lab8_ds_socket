package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NewsClient {
    final static Scanner scanner = new Scanner(System.in);
    static Socket socket = null;
    static PrintWriter out = null;
    static BufferedReader in = null;

    public static void addNewsCategory() {
        System.out.println("Enter category ID: ");
        String newCategoryId = scanner.nextLine();
        System.out.println("Enter category name: ");
        String newCategoryName = scanner.nextLine();
        out.println(generateLineWithSeparator("1", newCategoryId, newCategoryName));
    }


    public static void deleteNewsCategory() {
        System.out.println("Enter category ID: ");
        String categoryId = scanner.nextLine();
        out.println(generateLineWithSeparator("2", categoryId));
    }

    public static void addNewsToCategory() {
        System.out.println("Enter news ID: ");
        String newsId = scanner.nextLine();
        System.out.println("Enter news title: ");
        String newsTitle = scanner.nextLine();
        System.out.println("Enter news category ID: ");
        String newsCategoryId = scanner.nextLine();
        out.println(generateLineWithSeparator("3", newsId, newsTitle, newsCategoryId));
    }

    public static void deleteNews() {
        System.out.println("Enter news ID: ");
        String newsId = scanner.nextLine();
        out.println(generateLineWithSeparator("4", newsId));
    }

    public static void editNews() {
        System.out.println("Enter news ID: ");
        String newsId = scanner.nextLine();
        System.out.println("Enter news title: ");
        String newNewsTitle = scanner.nextLine();
        System.out.println("Enter news text: ");
        String newNewsText = scanner.nextLine();
        out.println(generateLineWithSeparator("5", newsId, newNewsTitle, newNewsText));
    }

    public static void countNewsInCategory() {
        System.out.println("Enter category ID: ");
        String categoryId = scanner.nextLine();
        out.println(generateLineWithSeparator("6", categoryId));
    }

    public static void searchNewsByTitle() {
        System.out.println("Enter news title: ");
        String newsTitle = scanner.nextLine();
        out.println(generateLineWithSeparator("7", newsTitle));
    }

    public static void getAllNewsInCategory() {
        System.out.println("Enter category ID: ");
        String categoryId = scanner.nextLine();
        out.println(generateLineWithSeparator("8", categoryId));
    }

    public static void getAllNewsCategories() {
        try {
            out.println(generateLineWithSeparator("9"));

            String response = in.readLine();
            String[] responseParts = parseLine(response);
            System.out.println("CLIENT:");
            System.out.println("Status: " + responseParts[0]);

            if (responseParts.length > 1) {
                for (int i = 1; i < responseParts.length; i++) {
                    System.out.println(responseParts[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showMenu() {
        System.out.println("1. Add a new news category");
        System.out.println("2. Delete a news category");
        System.out.println("3. Add news to a category");
        System.out.println("4. Delete news");
        System.out.println("5. Edit news");
        System.out.println("6. Count total number of news in a category");
        System.out.println("7. Search for news by title");
        System.out.println("8. Get all news in a category");
        System.out.println("9. Get all news categories");
        System.out.println("10. Exit");
    }

    public static String generateLineWithSeparator(String... args) {
        StringBuilder line = new StringBuilder();
        for (String arg : args) {
            line.append(arg).append("%");
        }
        return line.toString();
    }

    public static String[] parseLine(String line) {
        return line.split("%");
    }

    public static void readResponse() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                String[] responseParts = parseLine(response);
                System.out.println("CLIENT:");
                System.out.println("Status: ");
                for (String responsePart : responseParts) {
                    System.out.println(responsePart);
                }

                // Проверка наличия строки, которая указывает на конец ответа
                if (response.contains("END_OF_RESPONSE")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String choice = "";
            while (true) {
                showMenu();
                choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> addNewsCategory();
                    case "2" -> deleteNewsCategory();
                    case "3" -> addNewsToCategory();
                    case "4" -> deleteNews();
                    case "5" -> editNews();
                    case "6" -> countNewsInCategory();
                    case "7" -> searchNewsByTitle();
                    case "8" -> getAllNewsInCategory();
                    case "9" -> getAllNewsCategories();
                    case "10" -> {
                        System.out.println("CLIENT: Exit");
                        out.println(generateLineWithSeparator("10"));
                        socket.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }

                readResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

