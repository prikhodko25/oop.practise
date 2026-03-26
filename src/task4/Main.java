package ex17;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Головний клас програми
 * 
 * @author Student
 * @version 3.0
 */
public class Main extends ex17.Main {
    
    public Main(View view) {
        super(view);
    }
    
    @Override
    protected void menu() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ViewTable viewTable = null;
        
        if (view instanceof ViewTable) {
            viewTable = (ViewTable) view;
        }
        
        do {
            System.out.println("\nВведіть команду:");
            System.out.println("q - вихід");
            System.out.println("v - переглянути результати");
            System.out.println("g - згенерувати дані");
            System.out.println("s - зберегти у файл");
            System.out.println("r - відновити з файлу");
            if (viewTable != null) {
                System.out.println("w - змінити ширину таблиці (поточна: " + viewTable.getWidth() + ")");
            }
            System.out.print("> ");
            
            try {
                s = in.readLine();
            } catch (IOException e) {
                System.out.println("Помилка: " + e);
                System.exit(0);
            }
            
            if (s == null || s.length() == 0) continue;
            
            switch (s.charAt(0)) {
                case 'q':
                    System.out.println("Вихід.");
                    break;
                case 'v':
                    view.viewShow();
                    break;
                case 'g':
                    view.viewInit();
                    view.viewShow();
                    break;
                case 's':
                    try {
                        view.viewSave();
                        System.out.println("Збережено.");
                    } catch (IOException e) {
                        System.out.println("Помилка: " + e);
                    }
                    break;
                case 'r':
                    try {
                        view.viewRestore();
                        System.out.println("Відновлено.");
                    } catch (Exception e) {
                        System.out.println("Помилка: " + e);
                    }
                    view.viewShow();
                    break;
                case 'w':
                    if (viewTable != null) {
                        System.out.print("Ширина (10-80): ");
                        try {
                            String widthStr = in.readLine();
                            if (widthStr != null && !widthStr.isEmpty()) {
                                int newWidth = Integer.parseInt(widthStr);
                                if (newWidth >= 10 && newWidth <= 80) {
                                    viewTable.setWidth(newWidth);
                                    System.out.println("Ширину змінено на " + newWidth);
                                    viewTable.viewShow();
                                } else {
                                    System.out.println("Помилка: ширина має бути від 10 до 80");
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Помилка: введіть ціле число");
                        } catch (IOException e) {
                            System.out.println("Помилка: " + e);
                        }
                    }
                    break;
                default:
                    System.out.println("Невідома команда.");
            }
        } while (s != null && s.charAt(0) != 'q');
    }
    
    public static void main(String[] args) {
        Main main = new Main(new ViewableTable().getView());
        main.menu();
    }
}
